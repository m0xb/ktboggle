package com.dzapp.ktboggle

import kotlin.math.roundToInt
import kotlin.math.sqrt

class BoggleGame {

    fun makeSquareBoard(s: String): Board {
        val size = sqrt(s.length.toDouble()).roundToInt()
        return Board(Array(size) { i ->
            Array(size) { j ->
                s[size*i + j]
            }
        })
    }

}

class Board(val letters: Array<Array<Char>>) {

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
    println("Hello, boggle!")
}
