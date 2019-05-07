package com.dzapp.ktboggle

class CLI(val playerName: String) {

    val handlers: MutableList<EventHandler> = mutableListOf()
    var doneAddingWords = false

    fun handleInputLine(line: String) {
        if (line.trim() == "") {
            return
        }
        when (line.trim()) {
            "." -> {
                handlers.forEach { h -> h.onDoneAddingWords(playerName) }
                doneAddingWords = true
            }
            else -> handlers.forEach { h -> h.onAddWord(playerName, line.trim()) }
        }
    }
}
