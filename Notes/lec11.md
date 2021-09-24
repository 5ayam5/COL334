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

# Lecture 11 (Router Architecture and IP Datagram)

## Basics
1. Routers have $n$ incoming and outgoing ports where $n\geq 2$
1. It has a switching fabric which handles the transfer from input to output port
1. There exists a routing processor which decides the control of the data
1. Efficient router processes data at "wire speed"

## Input Port Functions
1. Line termination (physical layer)
1. Link layer protocol then receives the data
1. Decentralised switching:
    - performs lookup, forwarding, queuing
    - lookup is done by "looking-up" header field values in the forwarding table (match + action)
        i. destination-based forwarding
        i. generalised forwarding (based on any set of header values)
    - queuing is done if input rate is faster than forwarding speed
    - aim is to prevent queuing as much as possible

### Destination Based Forwarding
1. Forwarding table has a map of range of destination addresses with link interface
1. Overlapping mapping may exist
1. To resolve overlap, the longer prefix mapping is used
1. Matching is done using Ternary Content Addressable Memories (TCAMs), it returns the address in 1 clock cycle

## Switching Fabrics
1. *Switching Rate* is rate at which packets can be tranferred from input to output
1. Ideal switching rate is $n$ times the line rate
1. It is of three types:
    i. Memory
        - single transfer at a time, links change via memory
        - first generation routers used this type
    i. Bus
        - common bus on which the packet travels on when moving from input to output
    i. Interconnection network
        - $n\times n$ connection for direct movement
        - parallel transfer by breaking and joining datagram also exists in more advanced versions using -
            a. smaller switches
            b. parallel planes

## Input Port Queuing

### Head of the Line (HOL) Blocking
Head of the queue at input blocks the flow of the packets after it whose output ports have no traffic

## Output Port Queuing
1. To prevent HOL, queuing is done at output
1. Drop policy has to be implemented
1. Priority scheduling can be done (but net-neutrality a big issue)

### Buffering in the Queue
1. Average buffering is usually $RTT\times C$ ($C$ is link capacity)
1. The recent recommendation is $\displaystyle\frac{RTT\cdot C}{\sqrt{n}}$
1. Too much buffering can lead to delays
1. Packets that can be potentially dropped can be marked so that the sender can identify the same and choose a different route in future (ECN flag in TCP connection)

### Packet Scheduling
1. FCFS policy
1. Priority-based
1. Round robin (input link-wise)
1. Weighted fair queuing (round robin with weights)

## Net Neutrality
1. No blocking
1. No throttling
1. No paid prioritization

## IP Datagram
1. Version - 3 bits
1. Header length - 5 bits
1. Type of service - 8 bits
1. Length (of data) - 16 bits
1. Identifier - 16 bits
1. Flags + fragment offset - 16 bits
1. Time to live - 8 bits
1. Upper layer (TCP/UDP) - 8 bits
1. Header checksum - 16 bits
1. Source IP - 32 bits
1. Destination IP - 32 bits
1. Options

Total length of IP header is 20 bytes + options
