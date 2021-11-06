---
geometry:
- top=25mm
- left=20mm
- right=20mm
- bottom=30mm
documentclass: extarticle
fontsize: 12pt
numbersections: true
title: Lecture 20 (Ethernet)
--- 

# Introduction
1. First widely used LAN technology
1. Single chip, multiple speeds
1. Initially the bus topology was used, and now switched topology is used

# Ethernet Frame Structure
1. Preamble - 7 bytes of $10101010$ followed by one byte of $10101011$
1. Destination address - 6 bytes
1. Source address - 6 bytes
1. Type
1. Data (payload)
1. CRC

# Properties
1. Connectionless - no handshaking
1. Unreliable - no ACKs or NAKs sent
1. Unslotted CSMA/CD with binary exponential backoff is used
1. Follows IEEE $802.3$ standards

# Ethernet Switch
1. It is an active device
1. It stores and forwards Ethernet frames
1. It examines incoming frame's MAC address and selectively forwards frame to one-or-more outgoing links using CSMA/CD
1. Works in a transparent manner, host doesn't know existence of switch
1. Switch is a plug-and-play device, no configuration needed

## Working of Switch
1. Identifies the neighbours
1. Broadcast data to all neighbours initially - flooding
1. Learns locations of addresses wrt ports with time
1. The table thus learnt is called Bridge (MAC Address) Learning Table

## Interconnecting Switches
Switches in this case as well learn that multiple nodes are located on a single link

# Virtual LANs (VLANs)
1. Different physical link but same logical link
1. Naive implementation will lead to multiple broadcasts and can lead to unauthorised snooping hence

## Port Based VLAN
Single physical switch having multiple VLAN units - traffic isolation

## Trunk Port
Links VLANs over multiple switches, prevents broadcasting (defined in $802.1q$ standard)

Header changes as:

1. Preamble, destination address, source address
1. 2-byte tag protocol identifier (value between 81-00)
1. Tag control information - 12 bit VLAN ID, 3 bit priority field
1. Type, payload, CRC

# Multiprotocol Label Switching
Goal is to have high-speed IP forwarding among network of MPLS capable routers using fixed length label instead of shortest prefix matching

# Datacenter Networks
1. Connecting servers which are located far away, effectively
1. Server racks are created and a Top of Rack (ToR) switch is created
1. This ToR switch is connected to a Tier-2 switch, which connects ~16 ToRs
1. These T-2 switches are connected to Tier-1 switches in turn

# Other Examples
1. Facebook F16 data center uses spine and fabric switches over ToR switches
1. Application layer routing is also performs similar to SDNs
1. RoCE: Remove DMA over Converger Ethernet
