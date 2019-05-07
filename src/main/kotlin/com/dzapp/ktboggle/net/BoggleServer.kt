package com.dzapp.ktboggle.net

import com.dzapp.ktboggle.Board
import com.dzapp.ktboggle.BoggleGame
import com.dzapp.ktboggle.CLI
import com.dzapp.ktboggle.EventHandler
import java.io.*
import java.net.ServerSocket
import java.net.Socket
import java.time.Instant
import java.util.regex.Pattern
import kotlin.concurrent.thread

class BoggleServer(val port: Int) {

    var nextClientId: Int = 1
    val clients: MutableMap<Int, Client> = mutableMapOf()
    val clientHandlers: MutableMap<Int, ClientHandler> = mutableMapOf()

    var serverThread: Thread? = null

    var currentClientId: Int? = null

    fun start() {
        print("Enter your player name: ")
        val playerName = readLine()
        if (playerName == null) {
            throw RuntimeException("EOF when reading player name")
        }

        println("[Main] Starting server on $port...")
        val serverSocket = ServerSocket(port)
        println("[Main] Server listening on $port!")

        serverThread = thread {
            while (true) {
                println("[Main] Waiting for connection on $port...")
                val clientSocket = serverSocket.accept()
                val client = Client(nextClientId++, Instant.now(), clientSocket)
                clients[client.clientId] = client

                println("[Main] Got client from ${client.socket.inetAddress} at ${client.connectedAt}")

                val newCli = CLI(playerName)
                if (currentClientId == null) {
                    currentClientId = client.clientId
                }
                val clientHandler = ClientHandler(client, newCli)
                clientHandlers[client.clientId] = clientHandler

                thread() {
                    clientHandler.work()
                }
            }
        }

        loop@ while (true) {
            val line = readLine()
            if (line == null) {
                println("[Main] Server console EOF. Exiting.")
                break
            } else if (line.startsWith(':')) {
                val tokens = line.split(Pattern.compile("\\s+"))
                when (tokens[0]) {
                    ":exit" -> {
                        println("[Main] Got exit command. Exiting.")
                        break@loop
                    }
                    ":clients" -> {
                        println("[Main] Clients are ${clients.values.map{c -> c.clientId}.joinToString()}")
                    }
                    ":help" -> {
                        println("[Main] Commands are :exit, :clients, :switch, :help")
                    }
                    ":switch" -> {
                        if (tokens.size < 2) {
                            println("[Main] ERROR: clientId argument required")
                        } else {
                            currentClientId = tokens[1].toInt()
                            println("[Main] Switched to client $currentClientId")
                        }
                    }
                    else -> {
                        println("[Main] ERROR: Unknown command: '${tokens[0]}'")
                    }
                }
            } else {
                val clientHandler = clientHandlers[currentClientId]
                clientHandler?.cli?.handleInputLine(line)
            }
        }

        serverSocket.close()
        // TODO clean up thread?
//        serverThread?.interrupt()
    }
}

class ClientHandler(val client: Client, val cli: CLI) {

    fun work() {
        val input = BufferedReader(InputStreamReader(client.socket.getInputStream()))
        val output = BufferedWriter(OutputStreamWriter(client.socket.getOutputStream()))

        println("[Client ${client.clientId}] Waiting for client player name...")
        val clientPlayerName = input.readLine()
        println("[Client ${client.clientId}] ClientPlayerName: '$clientPlayerName'")
        println("[Client ${client.clientId}] Sending name to client...")
        output.write(cli.playerName + "\n")

        val game = BoggleGame.newGame(listOf(cli.playerName, clientPlayerName), 4, 4)
        cli.handlers.add(game.makeHandler())

        println("[Client ${client.clientId}] Generated board. Sending to client...")
        output.write(Protocol.encodeBoard(game.board) + "\n")
        output.flush()
        println("[Client ${client.clientId}] Board:\n${game.board.render()}")

        val clientWords = Protocol.decodeWordSet(input.readLine())

        val yourWords = game.words[cli.playerName]!!
        output.write(Protocol.encodeWordSet(yourWords) + "\n")
        output.flush()

        println("Your words (${yourWords.size}):")
        println(yourWords.joinToString("\n"))
        println()

        println("${clientPlayerName}'s words (${clientWords.size}):")
        println(clientWords.joinToString("\n"))
        println()


//        while (true) {
//            println("[Client ${client.clientId}] Available bytes: ${input.available()}")
//            if (input.available() > 0) {
//                println("[Client ${client.clientId}] Loop. Available bytes: ${input.available()}")
//                while (input.available() > 0) {
//                    val b = input.read()
//                    println("[Client ${client.clientId}]    Byte: $b")
//                    println()
//                }
//            }
//
//            Thread.sleep(1000)
//        }
    }
}

data class Client(
    val clientId: Int,
    val connectedAt: Instant,
    val socket: Socket
)
