---
geometry:
- top=25mm
- left=20mm
- right=20mm
- bottom=30mm
documentclass: extarticle
fontsize: 12pt
numbersections: true
title: Lecture 26 (E-Mail Security and TLS)
--- 

# Confidentiality
1. Alice generates a symmetric key
1. Encrypt the email using this key
1. Encrypt the key using Bob's public key
1. Send both the encrypted email and key to Bob

# Integrity
1. Hash of the email is encrypted with Alice's private key and then with they symmetric key
1. Everything is sent to Bob

# Transport Layer Security (TLS)
1. Secure Socket Layer (SSL) has been deprecated in 2015
1. TLS provides confidentiality, integrity and authentication
1. Works on port $443$ of HTTP

## What's Needed
1. Handshake
1. Key derivation
1. Data transfer
1. Connection closure

## Initial Handshake
1. Standard TCP SYN-ACK happens and TLS hello is done along with ACK of SYNACK
1. Alice sends public key certificate to Bob
1. Bob returns $K_A^+(MS) = EMS$ (Master Secret key) aftere verifying the public key
1. We have 3 RTTs before any data sharing can happen

## Keys
1. $K_c$: encryption key for data from client to server
1. $M_c$: Message Authentication Code (MAC) for data from client to server
1. $K_s$: encryption key for data from server to client
1. $M_s$: MAC key for data from server to client
1. They are derived from $MS$ using a Key Derivation Function (KDF)

## Data Transfer
1. Break stream into a series of records
1. Each record carries a MAC encrypted using $M_c$
1. The entire record is encrypted using $K_c$
1. TLS sequence numbers are used
1. Similar procedure is used for server-to-client

## Connection Closure
1. Record type is used
1. Type $0$ for data and type $1$ for closure

## TLS: $1.3$ Ciper Suite
1. Limited cipher suite choice than TLS $1.2$
1. Only $5$ choices rather than $37$ choices
1. Requires Diffie-Hellman (DH) for key exchange, rather than DH or RSA
1. Combined encryption and authentication algorithm
1. HMAC (Hashed MAC) uses SHA

### Steps - 1 RTT for Handshake
1. Client TLS hello message
    - Guesses key agreement protocol, parameters
    - Indicates cipher suites it supports
1. Server hello
    - Selected cipher suite
    - DH key agreement protocol, parameters

### Steps - 0 RTT for Handshake
1. Client hello
    - Resume previous conversation using the same keys and send encrypted application data
1. Server hello
    - Reply to the application data
1. Client and server still send the same information as in the previous part too
1. This is no longer used since this is prone to replay attacks

## QUIC
1. Used hy HTTP/3
1. Alternative to TLS and quicker

# IP Security - IP Sec
It is of two types:

1. Transport mode - only datagram payload is encrypted
1. Tunnel mode - entire datagram is encrypted, encrypted datagram is encapsulared in new datagram with new IP header and tunneled to destination

## Protocols
1. Authentication Header (AH) - authentication, data integrity but not confidentiality
1. Encapusulation Securty Protocol (ESP) - provides all three including confidentiality

## Security Associations (SAs)
1. We need handshake before sending data, i.e., SA (which is directional)
1. Sending and receiving entities maintain state information about SA
1. This is different from IP which was connectionless
1. Data stored is:
    a. 32-bit identifier - Security Parameter Index (SPI)
    a. Origin SA interface
    a. Destination SA interface
    a. Encryption key
    a. Type of encryption used
    a. Type of integrity check used
    a. Authentication key

## IP Sec Datagram
1. new IP header
1. ESP header - SPI and sequence number
1. original IP header
1. original IP datagram payload
1. ESP trailer - padding, pad length, next header
1. ESP auth - MAC with shared secret key (acts as payload)

IP section along with ESP trailer is encrypted

## IPsec Sequence Number
1. For new SA, sequence number is initialised to $0$
1. This prevents sniffing and replaying packet

## IPsec Security Databases
1. Security Policy Database
    - For given datagram, sender needs to know if it should use IP sec
    - Policy is stored in Secure Policy Database
    - SPD: what to do
1. Security Association Database
    - Endpoint holds SA state in SAD
    - When sending IPsec datagram, router accesses SAD to determine how to process datagram
    - On receiving datagram, router examines SPI and indexes SAD with SPI and processes datagram accordingly
    - SAD: how to do it

# Internet Key Exchange
1. Establishing IPsec SAs for multiple nodes is impractical in VPN or similar situations
1. Instead IPsec IKE is used
