# Using Websockets

## What are websockets ?
This application relies on the websockets protocol to have double-sided
client to server communication. Websockets is a protocol that is written
above the TCP stack. so it's analogous to http, however http only supports
the server responding to messages from the client, websockets enables a server and a client
to establish a connection where both the client and server can initiate communication.
Therefore the server can notify the client when changes occur such as receiving a message from 
another client.

This enables us to create multiple features such as:
+ Chat (without manually reloading the page to check for messages)
+ ProgressBar (which updates automatically without having to reload the page)

## How to setup the application to use websockets.
