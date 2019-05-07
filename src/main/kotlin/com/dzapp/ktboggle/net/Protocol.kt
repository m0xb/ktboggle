package com.dzapp.ktboggle.net

import com.dzapp.ktboggle.Board
import com.dzapp.ktboggle.BoggleGame

object Protocol {

    fun encodeBoard(board: Board): String {
        return "${board.getWidth()},${board.getHeight()}," + board.letters.joinToString("") { row -> row.joinToString("") }
    }

    fun decodeBoard(encodedBoard: String): Board {
        val (w, h, letters) = encodedBoard.split(',')
        return BoggleGame.makeBoard(letters, w.toInt(), h.toInt())
    }

    fun encodeWordSet(words: Set<String>): String {
        return words.joinToString("|")
    }

    fun decodeWordSet(s: String): Set<String> {
        if (s.length == 0) {
            return emptySet()
        }
        return s.split('|').toSet()
    }
}
