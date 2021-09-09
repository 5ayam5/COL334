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

# Lecture 8 (Transport Layer pt 3)

## TCP
1. Point-to-point
2. Fully duplex
3. Notion of Maximum Segment Size (MSS) exists - maximum message size (doesn't count header)
4. Cumulative ACKs
5. Pipelining exists
6. Connection oriented - handshaking happens
7. Flow control

## TCP Segment Structure
Header contains the following:

1. Source port number (16 bits)
1. Destination port number (16 bits)
1. Sequence number (32 bits) - stores the byte number instead of segment number
1. Acknowledgement number (32 bits) - next byte number which is needed
1. Header length (atmost 16 bits)
1. C, E (1 bit each) - congestion notification
1. U[rgent] flag (1 bit)
1. Acknowledgement flag (1 bit)
1. PSH [push] flag (1 bit)
1. RST, SYN, FIN (1 bit each)
1. Receive window (16 bits) - number of bytes receiver willing to accept
1. Checksum (16 bits)
1. Urgent data pointer (16 bits)
1. TCP options (variable length)

Without TCP options, header is of 20 bytes

## TCP ACK
Out of order segment are handled by the implementor and no formal specification is present

## TCP Timeout
TCP estimates RTT by considering a SampleRTT value from the time taken for ACK to reach, this values varies a lot and hence an estimatedRTT is needed:
$${estimatedRTT}=(1-\alpha){estimatedRTT}+\alpha{SampleRTT}$$
This evaluation gives EWMA (exponential weighted moving average) and $\alpha$ is usually taken as $0.125$. Timeout interval is given as:
$${TimeoutInterval}={estimatedRTT}+4{deviationRTT}$$
$deviationRTT$ is calculated using EWMA of deviation of $SampleRTT$ from $estimatedRTT$

## TCP Sender (Simplified)
1. On data from application:
    - Create segment with sequence number
    - Start timer for oldest unACKed packet
2. When timeout:
    - Retransmit segment which caused timeout
    - Restart timeout
3. On ACK:
    - Update list of ACKed packets
    - Restart timer for unACKed packets

## TCP Receiver
1. Receiving in-order segment
    - delayed ACK
    - wait for 500ms for more segments
2. 2nd segment arrived with another segment's ACK pending
    - send cumulative ACK for both segments
3. Out of order segment
    - send duplicate ACK for the missing segment
4. Segment that shifts the missing gap rightwards
    - send ACK

## TCP Fast Retransmit
If sender receives 3 additional ACKs for same data, resend unACKed segment with smalles sequence number (since that segment has mostly been lost)

## TCP Flow Control
`rwnd` field in the header contains the available empty buffer size, unACKed data is limited to `rwnd`

## TCP Connection Management

### 3 Way Handshake
1. Inital sequence number is set randomly (x), SYN bit is set in the header
2. Server (receiver) of the connection sends a packet with a random sequence number (y), SYN and ACK bit set (ACK number = x + 1)
3. On receiving SYNACK, send ACK and request for y+1

### Closing Connection
FIN bit is set to 1 and ACK for this packet is sent
