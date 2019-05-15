package com.dzapp.ktboggle

import kotlin.test.*

class AppTest {

    @Test fun testMakeSquareBoard() {
        val board = BoggleGame.makeSquareBoard("XBOY")

        assertEquals(board.letters[0][0], 'X', "app should have a greeting")
        assertEquals(board.letters[0][1], 'B', "app should have a greeting")
        assertEquals(board.letters[1][0], 'O', "app should have a greeting")
        assertEquals(board.letters[1][1], 'Y', "app should have a greeting")
    }

    @Test fun test2x2HasWordOneLetterDoesntExist() {
        val board = BoggleGame.makeSquareBoard("EYES")

        assertFalse(board.hasWord("A"))
        assertFalse(board.hasWord("D"))
    }

    @Test fun test2x2HasWordDoesntExistWithoutReusingLetter() {
        val board = BoggleGame.makeSquareBoard("NSSN")
        assertFalse(board.hasWord("SSS"))
    }

    @Test fun test2x2HasWordOneLetterDoesExist() {
        val board = BoggleGame.makeSquareBoard("EYES")

        assertTrue(board.hasWord("E"))
        assertTrue(board.hasWord("Y"))
        assertTrue(board.hasWord("S"))
    }

    @Test fun test2x2ThreeLetterWordsExist() {
        val board = BoggleGame.makeSquareBoard("TEHM")
        assertTrue(board.hasWord("THE"))
        assertTrue(board.hasWord("MET"))
        assertTrue(board.hasWord("HEM"))
    }

    @Test fun test2x2FourLetterWordExists() {
        val board = BoggleGame.makeSquareBoard("TRUE")
        assertTrue(board.hasWord("TRUE"))
    }

    @Test fun test3x3NineLetterWordExists() {
        val board = BoggleGame.makeSquareBoard("DESREPATE")
        assertTrue(board.hasWord("DESPERATE"))
    }

    @Test fun tes4x4HasWordOneLetterDoesExist() {
        val board = BoggleGame.makeSquareBoard(s=
            "EYES" +
            "YELL" +
            "COME" +
            "HOME")

        assertTrue(board.hasWord("EYES"))
        assertTrue(board.hasWord("EYESLLEY"))
        assertTrue(board.hasWord("EEME"))
    }
}
