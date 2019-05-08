# Boggle (in Kotlin!)

## Overview

This is a simply implementation of the game of Boggle, written in Kotlin.

## Running

As a server (binds to a port and waits for an opponent client to connect):
```
gradle run --console=plain --args 'server'
```

As a client (connects to the given IP and port):
```
gradle run --console=plain --args 'connect localhost'
```

Run both client and server in separate tabs and you can play against yourself.

## Testing

```
gradle test
```

## Protocol

The client and server communicate over a TCP socket with a text-based protocol. Messages are sent as one line. The default port is 42266.

Currently, the game only supports one round and is fairly inflexible in how it's played.

1. The player connects to the server and sends his/her name.
1. The player waits for the server to send the other player's name and the board.
1. The player enters words until they can think of no more, and then signals they are done.
1. The server sends the set of words found by the other player.
1. The client may now compare both sets of words and display a score.

### Network Sequence

|Actor|Action|Data|
|-----|------|-------|
|Server|LISTEN||
|Client|CONNECT||
|Client|MESSAGE|string - client player's name|
|Server|MESSAGE|string - other player's name|
|Server|MESSAGE|board - server generated board|
|Client|MESSAGE|wordset - client's found words|
|Server|MESSAGE|wordset - server's found words|

### Message Types

All messages are one line (terminated with a line feed character, `\n`), UTF-8 encoded strings. At this point, a message's type can't be inferred from the message data. Instead, the program must know what type to expect from the peer based on the current state of the game.

|Type|Description|
|----|-----------|
|string|The string itself.|
|board|A string of the form `W,H,<letters>` where `<letters>` is `W`*`H` characters long, and represents the letters of the board going left-to-right, top-to-bottom.|
|wordset|A pipe-delimited list of words. May be empty.|
