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

# Lecture 10 (Introduction to Network Layer)

## Router
1. Input port
2. Output port
3. Switching
4. Buffer management
5. Scheduling

## Internet Protocol (IP)
1. Datagram format
2. Addressing
3. Network address translation
4. IPv6 is the new version

## Generalised Forwarding - Software Defined Networking
1. Match+action
2. OpenFlow - match+action in action

## Network Layer Services and Protocol
1. Takes data from transport layer and transmits it to the destination
2. Present in all points on the network (unlike transport layer which is only present at the ends)
3. Routers examine the header field in all IP datagrams and move them from input port to the output port

## Functions of Network Layer
1. Forwarding data from router's input link to appropriate router's output link
2. Routing

## Data Plane
1. It is a local, per-router function
2. It determines how datagram arriving on input port is forwarded to output port

## Control Plane
1. Network-wide logic
2. Determines how datagram is routed and prevents loops
3. Two approaches exist:
    i. Traditional routing algorithms (implemented in routers)
    i. SDNs (implemented in remote servers)

### Per-Router Control Plane
Forwarding table is maintained which decides the next router to send the data to

### SDN
Remote controller computes and supplies the routing table to the routers

## Access Networks
If access network is "switched":

1. Similar to a packet-switched network (or)
2. Layer 2 connectivity

If it is "shared", sharing of data needs to be figured out: wireless or cable

## What Should be the Promises given by Network Layer?
Points to ponder are:

1. Delivery within a timeout
2. In-order delivery
3. Minimum bandwidth for a "flow"
4. Spacing between packets is maintained

## What is Actually Promised?
Service model is "best-effort":

1. No bandwidth guarantee
2. Loss is possible
3. Ordering can change
4. No guarantee on timing

## Comparison
Network architecture | Service Model | Bandwidth | Loss | Order | Timing
--- | --- | --- | :---: | :---: | :---:
Internet | best effort | none | no | no | no
ATM | constant bitrate | constant rate | yes | yes | yes
ATM | available bitrate | guaranteed minimum | no | yes | no
Internet | Intserv Guaranteed | yes | yes | yes | yes
Internet | Diffserv | possible | possibly | possibly | no

## Features about Best Effort
1. Simplicity
1. Sufficient provisioning of bandwidth is "good enough" for "most of the time"
1. CDN-like implementations helped
1. Congestion control of elastic services allows best effort to work
