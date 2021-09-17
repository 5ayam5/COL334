# Assignment 2 - Simple Chat Application
## Sayam Sethi (2019CS10399)

### Overview
The application uses a simple protocol for communication which involves running two sockets (one for sending and one for receiving) for each client as threads. The application supports unicast and broadcast messages (server is designed in such a way that it can be easily be extended to multicast).

### Server
To run the server:
```
> make runServer [port=<port>]
```

### Client
To run the client:
```
> make runClient username=<username> [port=<port> [address=<address>]]
```
To send a message via the command line, use the syntax:
```
@<target/ALL> <message>
```
