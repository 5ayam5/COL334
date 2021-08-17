---
geometry:
- top=25mm
- left=20mm
- right=20mm
- bottom=30mm
documentclass: extarticle
fontsize: 12pt
numbersections: true
---

# Lecture 3 (Application Layer)

When creating a network app it is enough to write programs that runs on the end systems and communicates over the network. The layered architecture helps in avoiding writing code to interact with *network-core devices* (such as switches and routers).

## Principles of Network Applications

### Network Paradigms

#### Client-Server Paradigm

##### Server

1. Always on host
2. Permanent IP address
3. Usually present in data centres

##### Client

1. Intermittently connected
2. May have dynamic IP addresses
3. Do not communicate directly with each other

#### Peer-to-Peer Paradigm
1. No always-on server
2. Direct communication
3. Peers request service from and provide service to other peers on the network (self-scalability)
4. Intermittent connection

### Process Communication
This is of two types:

1. Inter-process communication (handled by the OS)
2. Inter-host communication (via networking)
    i. Client process: initiates communication
    i. Server process: waits to be contacted

*Note:* Applications with P2P architecture have both client and server processes.

### Socket
1. Process sends/receives messages to/from its socket
2. It is analogous to a door:
    - Sending process *shoves* message out of the door
    - Relies on transport infrastructure on the other side of door to deliver message to socket at receiving process
    - Two sockets involved - sending and receiving sockets

### Addressing Processes
1. Each host device has a unique 32-bit IP address, that acts as an *identifier*
2. TO address the issue of multiple processes running on the same host, the concept of **port number** is used

### Information in the Header(?)
1. Type of message exchanged: request/response
2. Message syntax: what fields present in the message
3. Message semantics: what do the fields mean
4. Rules: when and how processes send and respond to messages

Various open-protocols defining the syntax for the above information are available:

- HTTP
- SMTP
- FTP

### Requirements of Network Applications
1. Data integrity: some require lossless transmission, some can tolerate losses
2. Delay: some require low delay to be effective
3. Throughput: some apps need a minimum throughput
4. Security

### Internet Transport Protocols

#### Transmission Control Protocol (TCP)
1. Reliable transport between sending and receiving processes
2. Flow control 9to not overwhelm the receiver
3. Congestion control
4. Connection-oriented: setup needed before communication
5. No timining, throughput, security guarantee

#### User Datagram Protocol (UDP)
1. Unreliable data transfer
2. Does not provide any other feature provided by TCP

## HTTP
1. Follows a **client-server model**
2. Uses **TCP protocol**:
    i. Client initiates TCP request to server at port $80$
    i. Server accepts TCP connection
    i. HTTP messages exchanged
    i. TCP connection closed
3. It is **stateless**: no information about the client is stored at the server
4. This has two types:
    i. **Non-Persistent HTTP** - atmost one object is sent for each connection
    i. **Persistent HTTP (HTTP 1.1)** - multiple objects can be sent over a single TCP connection
5. Two types of HTTP messages:
    i. **Request**
    i. **Response**

### HTTP Request
```
<method> <URL> <version> \r\n
<header field name> <value> \r\n
.
.
.
<header field name> <value> \r\n
\r\n
<body>
```

There are two types of methods:

1. **POST method** - data is sent in the body of the request
2. **GET method** - data is sent in the URL field  
(There is a concept of **conditional GET** in which the response is sent only if content is modified since the requested date)
3. **HEAD method** - only request for headers (as response to a *GET method*)
4. **PUT method** - replaces file at URL with body of *POST method*

### HTTP Response
This contains the response code of the request.

### Storing Information about Client
This is done using **cookies**. This is made up of four components:

1. Header line of HTTP response
2. Header line of next HTTP request
3. Cookie is stored on client and is managed by the browser
4. Corresponding information also stored in server backend

### Web Cache (Proxy Servers)
1. Browser is configured to point to a cache
2. All HTTP requests are sent to the cache:
    i. *if* request is found, cache returns object to client
    i. *else* request is sent to origin server and response cached + returned to client




















