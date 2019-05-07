package com.dzapp.ktboggle

abstract class EventHandler {
    open fun onAddPlayer(playerName: String) {}
    open fun onAddWord(playerName: String, word: String) {}
    open fun onDoneAddingWords(playerName: String) {}
}
