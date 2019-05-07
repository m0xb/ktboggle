# Boggle (in Kotlin!)

## Overview

This is a simply implementation of the game of Boggle, written in Kotlin.

## Running

As a server (binds to a port and waits for an opponent client to connect):
```
gradle run --console=plain --args 'server 42266'
```

As a client (connects to the given IP and port):
```
gradle run --console=plain --args 'connect localhost 42266'
```

Run both client and server in separate tabs and you can play against yourself.

## Testing

```
gradle test
```
