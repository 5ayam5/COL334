---
geometry:
- top=25mm
- left=20mm
- right=20mm
- bottom=30mm
documentclass: extarticle
fontsize: 12pt
numbersections: true
title: Lecture 18 (Link Layer)
--- 

# Introduction
1. Hosts and routers are called nodes
1. Links can be wired, wireless or via LAN
1. The data that is transmitted on this layer is called **frame**

# Services
1. Encapsulates datagram into frame adding header and trailer
1. Provides channel access if shared medium
1. Uses MAC address to identify source and destination

It also offers the following:
1. Flow control
1. Error detection
1. Error correction
1. Half and full duplex

# Where is it Implemented
1. Needs to be implemented in each and every host
1. It is implemented in the NIC or on a chip
1. It attaches into host's system buses
1. It is a combination of hardware, software and firmware

# Error Detection
1. Error detection and correction (EDC) bits are present
1. $D$ is the data which is protected by error checking, having $d$ data bits
1. If all the received bits in $D'$ are *OK*, then the data is passed upwards, else error is detected

## Methods for Error Checking

### Parity Checking
EDC bit is to maintain the parity of even (or odd) number of set bits

### Two Dimensional Bit Parity
1. The data is split into 2D and parity is set for each row, each column and the entire data
1. The error can then be identified and it is then corrected

### Checksum
Perform 1's complement sum to compute the checksum and add it to the header

### Cyclic Redudancy Check (CRC)
1. $D$ is given (data bits)
1. $G$, bit pattern (generator) of $r+1$ bits is also given
1. $<D, R> = D\cdot 2^r \text{xor} R$ ($R$ is $r$ CRC bits)
1. Choose $R$ such that $<D, R>$ is exactly divisible by $G (\mod 2)$
$$D\cdot 2^r \text{xor} R = nG$$
$$D\cdot 2^r = nG \text{xor} R$$
This implies that the remainder on dividing $D\cdot 2^r$ by $G$ should be $R$.
1. Standard values of $G$ are available

## Multiple Access Protocols
There are two types of links:

1. Point-to-point
1. Broadcast

Single shared broadcast channel can lead to collision when a node receives multiple signals at the same time. Additionally, any communication regarding the channel sharing must use the channel itself since there is no other way of communication.

### Ideal Multiple Access Protocol
The desiderata of a Multiple Access Channel (MAC) is:

1. When one node wants to transmit, it can send at rate $R$
1. When $M$ nodes want to transmit, each can send at an average rate of $R/M$
1. It should be fully decentralised
1. Simplicity

### MAC Protocols
1. Channel partitioning:
  - Divide channel into smaller pieces
  - Allocate piece to node for exclusive use
1. Random access:
  - Allow collisions
  - Implement recovering from such collisions
1. Taking turns
