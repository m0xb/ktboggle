package com.dzapp.ktboggle

import com.dzapp.ktboggle.net.BoggleClient
import com.dzapp.ktboggle.net.BoggleServer
import java.lang.RuntimeException
import kotlin.math.roundToInt
import kotlin.math.sqrt

object BoggleLauncher {
    const val DEFAULT_PORT = 42266

    fun launch(args: Array<String>) {
        if (args.size > 0) {
            when(args[0]) {
                "server" -> {
                    val port = if (args.size > 1) args[1].toInt() else DEFAULT_PORT
                    BoggleServer(port).start()
                    return
                }
                "connect" -> {
                    val host = if (args.size > 1) args[1] else throw RuntimeException("Missing argument: host")
                    val port = if (args.size > 2) args[2].toInt() else DEFAULT_PORT
                    BoggleClient(host, port).run()
                    return
                }
            }
        }

        println("Boggle CLI")
        println()
        println("usage:")
        println("    boggle server [PORT]")
        println("    boggle connect HOST [PORT]")
    }

}

class BoggleGame(val players: List<String>, val board: Board) {

    val words: MutableMap<String, MutableSet<String>> = mutableMapOf()

    init {
        players.forEach { playerName ->
            words[playerName] = mutableSetOf()
        }
    }

    fun addWord(playerName: String, word: String) {
        words[playerName]!!.add(word)
    }

    fun makeHandler(): EventHandler {
        return object: EventHandler() {
            override fun onAddWord(playerName: String, word: String) {
                this@BoggleGame.addWord(playerName, word)
            }
        }
    }

    companion object {
        fun newGame(players: List<String>, boardWidth: Int, boardHeight: Int): BoggleGame {
            return BoggleGame(players, randomBoard(boardWidth, boardHeight))
        }

        fun makeSquareBoard(s: String): Board {
            val size = sqrt(s.length.toDouble()).roundToInt()
            return makeBoard(s, size, size)
        }

        fun makeBoard(s: String, boardWidth: Int, boardHeight: Int): Board {
            if (s.length != boardWidth * boardHeight) {
                throw IllegalArgumentException("String length must equal WxH (${s.length} != $boardWidth x $boardHeight)")
            }
            return Board(Array(boardHeight) { i ->
                Array(boardWidth) { j ->
                    s[boardWidth*i + j]
                }
            })
        }

        fun randomBoard(boardWidth: Int, boardHeight: Int): Board {
            return Board(Array(boardHeight) { i ->
                Array(boardWidth) { j ->
                    ('A'..'Z').random()
                }
            })
        }
    }
}

class Board(val letters: Array<Array<Char>>) {

    fun getWidth(): Int {
        return letters[0].size
    }

    fun getHeight(): Int {
        return letters.size
    }

    fun render(): String {
        return letters.joinToString("\n") { row ->
            row.joinToString("")
        }
    }

    fun hasWord(word: String): Boolean {
        for (i in 0 until letters.size) {
            for (j in 0 until letters[i].size) {
                if (hasWordFrom(word, Pair(j, i), emptySet())) {
                    return true
                }
            }
        }
        return false
    }

    private fun hasWordFrom(word: String, coord: Pair<Int, Int>, used: Set<Pair<Int, Int>>): Boolean {
        if (word.isEmpty()) {
            return true
        }
        if (word[0] != letters[coord.first][coord.second]) {
            return false
        }
        for (adjacent in adjacents(coord).subtract(used)) {
            // assert: adjacent not in (used | {coord})
            if (hasWordFrom(word.substring(1), adjacent, used.union(setOf(coord)))) {
                return true
            }
        }
        return false
    }

    private fun adjacents(coord: Pair<Int, Int>): List<Pair<Int, Int>> {
        val x = coord.first
        val y = coord.second
        val h = letters.size
        val w = letters[0].size

        val adjacents = mutableListOf<Pair<Int, Int>>()
        for (i in -1..1) {
            for (j in -1..1) {
                if (i == 0 && j == 0) {
                    continue
                }
                if ((x+j) !in 0 until w) {
                    continue
                }
                if ((y+i) !in 0 until h) {
                    continue
                }
                adjacents.add(Pair(x+j, y+i))
            }
        }
        return adjacents
    }
}

fun main(args: Array<String>) {
    BoggleLauncher.launch(args)
}
