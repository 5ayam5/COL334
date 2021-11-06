---
geometry:
- top=25mm
- left=20mm
- right=20mm
- bottom=30mm
documentclass: extarticle
fontsize: 12pt
numbersections: true
title: Lecture 19 (MAC Protocols)
--- 


# Channel Partition Protocols

## TDMA (Time Division Multiple Access)
Divide access to channel in time slots

## FDMA (Frequency Division Multiple Access)
Divide access to channel in frequency slots

# Random Access Protocols
Transmits at full channel data rate

## Slotted ALOHA
1. Assumes that all frames of same size
1. Time divided into equal size slots
1. Nodes start to transmit only at slot beginning
1. Nodes are synchronized
1. If multiple nodes transmit in same slot, all nodes detect collision
1. If collision occurs, retransmission happens in subsequent slot with probability $p$
1. It has disadvantage of clock synchronisation and wastage of the time slot in case of collision

### Efficiency
$$\text{success} = p(1-p)^{N-1}$$
$$\text{success}_\text{any} = Np(1-p)^{N-1}$$
$$\max{\text{efficiency}} = \max_p Np(1-p)^{N-1}$$
This max efficiency is equal to $1/e$

## Pure ALOHA
1. Similar to slotted but no synchronisation and time for transmission can differ
1. The efficiency is half of the efficiency of pure ALOHA

## CSMA/CD (Carrier Sense Multiple Access/Collision Detection)
1. Sense the channel before transmitting, prevents collisions
1. More advanced version has CD, and colliding transmission is aborted to reduce channel wastage
1. CSMA/CD is difficult to implement in wireless networks

### Algorithm
1. Receive datagram, create frame(s)
1. If channel busy, wait else start frame transmission
1. If transmission successful, finish transmission
1. Else, abort and send jam signal
1. After $m\textsuperscript{th}$ collision, choose $K$ randomly from $\{0, 1, 2, 3, \ldots, 2^m - 1\}$ and wait for $512K$ bit times and then reattempt transmission

### Efficiency
$$\text{efficiency} = \frac{1}{1 + 5t_{prop}/t_{trans}}$$
Where $t_{prop}$ is max propagation delay and $t_{trans}$ is transmission delay (for max-size frame).

# "Taking Turns" Protocol

## Polling
1. Master node *invites* other nodes to transmit in turn
1. Drawbacks are:
  - polling overhead
  - latency
  - single point of failure

## Token Passing
1. Control token is passed from one node to the next one sequentially
1. Similar drawbacks as previous part (token overhead instead of polling)


# Wireless Link Characteristics
1. Signal strength decreases with increasing distance
1. Interference from other sources
1. Multipath propagation (same data can arrive multiple times)
1. Hidden terminal problem: two nodes may not be able to detect the presence of the other and hence CSMA might not work

## $802.11$ LAN/WiFi Architecture
1. Host communicates with base station
1. Base station is the Access Point (AP)
1. Basic Service Set (BSS) contains:
  - wireless hosts
  - AP
  - ad hoc mode (hosts only)

### CSMA/CA (Carrier Sense Multiple Access/Collision Avoidance)
1. Since it is difficult to detect collisions, avoidance is done
1. If channel is sensed to be idle for WT1, entire frame is transmitted
1. Else, start a random backoff time and transmit when timer expires
1. Wait for ACK and if none received, increase random backoff interval and re-wait
1. On receiver's end, return ACK after WT2
1. Sender *reserves* channel use for data frames using small reservation packets
1. Small request-to-send (RTS) packets are transmitted first to BS
1. BS broadcasts clear-to-send (CTS) in response to RTS
