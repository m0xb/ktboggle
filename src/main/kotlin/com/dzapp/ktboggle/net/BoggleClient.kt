package com.dzapp.ktboggle.net

import com.dzapp.ktboggle.Board
import com.dzapp.ktboggle.BoggleGame
import com.dzapp.ktboggle.CLI
import com.dzapp.ktboggle.EventHandler
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.lang.RuntimeException
import java.net.Socket

class BoggleClient(val host: String, val port: Int) {

    val CLIENT_AGENT = "boggle com.dzapp.ktboggle v0.1"

    fun run() {
        println("Attempting connection to boggle server at $host:$port...")
        val socket = Socket(host, port)
        println("Successfully connected to boggle server at $host:$port!")
        println()

        print("Enter your player name: ")
        val playerName = readLine()!!
        println("Welcome, $playerName!")
        println()

        val input = BufferedReader(InputStreamReader(socket.getInputStream()))
        val output = BufferedWriter(OutputStreamWriter(socket.getOutputStream()))
        output.write("$playerName\n")
        output.flush()

        val otherPlayerName = input.readLine()
        println("You are playing with: $otherPlayerName")
        println()

        val encodedBoard = input.readLine()
//        println("Encoded board: $encodedBoard")

        val board = Protocol.decodeBoard(encodedBoard)
        println("Board:\n${board.render()}")

        val game = BoggleGame(listOf(playerName, otherPlayerName), board)

        val cli = CLI(playerName)
        cli.handlers.add(game.makeHandler())
//        cli.handlers.add(object: EventHandler() {
//            override fun onDoneAddingWords(playerName: String) {
//            }
//        })
        while (true) {
            print("Enter a word: ")
            val line = readLine()!!
            cli.handleInputLine(line)
            if (cli.doneAddingWords) {
                println("Okay. Waiting for other player to finish.")
                println()
                break
            }
        }

        val yourWords = game.words[playerName]!!
        output.write(Protocol.encodeWordSet(yourWords) + "\n")
        output.flush()

        val otherPlayerWords = Protocol.decodeWordSet(input.readLine())

        println("Your words (${yourWords.size}):")
        println(yourWords.joinToString("\n"))
        println()

        println("${otherPlayerName}'s words (${otherPlayerWords.size}):")
        println(otherPlayerWords.joinToString("\n"))
        println()

        socket.close()
    }
}
