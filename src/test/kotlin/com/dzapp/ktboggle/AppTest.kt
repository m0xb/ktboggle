package com.dzapp.ktboggle

import kotlin.test.*

class AppTest {

    @Test fun testMakeSquareBoard() {
        val classUnderTest = BoggleGame()
        val board = classUnderTest.makeSquareBoard("XBOY")

        assertEquals(board.letters[0][0], 'X', "app should have a greeting")
        assertEquals(board.letters[0][1], 'B', "app should have a greeting")
        assertEquals(board.letters[1][0], 'O', "app should have a greeting")
        assertEquals(board.letters[1][1], 'Y', "app should have a greeting")
    }

    @Test fun test2x2HasWordOneLetterDoesntExist() {
        val classUnderTest = BoggleGame()
        val board = classUnderTest.makeSquareBoard("EYES")

        assertFalse(board.hasWord("A"))
        assertFalse(board.hasWord("D"))
    }

    @Test fun test2x2HasWordOneLetterDoesExist() {
        val classUnderTest = BoggleGame()
        val board = classUnderTest.makeSquareBoard("EYES")

        assertTrue(board.hasWord("E"))
        assertTrue(board.hasWord("Y"))
        assertTrue(board.hasWord("S"))
    }

    @Test fun tes4x4HasWordOneLetterDoesExist() {
        val classUnderTest = BoggleGame()
        val board = classUnderTest.makeSquareBoard(s=
            "EYES" +
            "YELL" +
            "COME" +
            "HOME")

        assertTrue(board.hasWord("EYES"))
        assertTrue(board.hasWord("EYESLLEY"))
        assertTrue(board.hasWord("EEME"))
    }
}
