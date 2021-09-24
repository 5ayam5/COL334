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

# Lecture 12 (DHCP, ARP, IPv6, SDNs)

## DHCP
1. Host broadcasts `DHCP discover` message (optional)
    - destination MAC = FF:FF:FF:FF:FF:FF
    - source IP = 0.0.0.0
    - destination IP = 255.255.255.255
1. DHCP server responds with address in `DHCP offer` message (optional)
1. Host sends `DHCP request` (source IP is still 0.0.0.0)
1. DHCP confirms the IP via `DHCP ack` (it also sends address of first-hop router, DNS server, network mask)
1. After the lease timeout, host requests for renewal (skipping the optional steps)

## Access Networks
1. Layer 2 communication can be done using MAC (Media Access Control) address
1. It is built-in for each device
1. IP to MAC conversion is done using Address Resolution Protocol (ARP)
    - Router sends a broadcast query asking the MAC address
    - Corresponding host responds

## IP Address
1. ICANN - Internet Corporation for Assigned Names and Numbers assigns the IP addresses through 5 Regional Registries (RRs)
1. Not enough IP addresses exist
    - The last chunks of IP addresses were allocated in 2011
    - Network Address Translation (NAT) exists
    - IPv6 has been introduced which uses a 128-bit space

### NAT
1. Public IP is different from local IP address
1. Identifying the internal IP is done by using different port numbers for the local hosts
1. Translation is: (source IP, port) $\to$ (NAT IP, new port)
1. Router maintains a NAT table for reverse translation
1. Drawbacks:
    - router should process only till layer 3
    - inherent property of functioning of IP is violated since TCP modified

## IPv6
1. IPv0 to IPv3 wasn't very widely used, similarly IPv5 was deprecated
1. IPv6 has 128 bit addressing
1. The protocol has a different treatment of "flows" and thus has a fixed length header of 40 bytes
1. No need of DHCP
1. It is more secure since communication is encrypted

### Header
1. Version - 4 bits
1. Traffic class - 8 bits
1. Flow label - 20 bits
1. Payload length - 16 bits
1. Next header - 8 bits
1. Hop limit - 8 bits
1. Source address - 128 bits
1. Destination address - 128 bits

### IPv6 Address
It is of three parts:

1. Global Routing (48 bits) -
    i. first three bits are 001 for global unicast
    i. 45 bits for global routing prefix
1. Subnet ID (16 bits)
1. Interface ID (64 bits)

#### Address Scope
1. Link-local - nodes on same subnet
1. Unique-local - private side addressing
1. Global (001)

#### Representation
1. x:x:x:x:x:x:x:x where x is 16-bit hexadecimal field
1. Successive fields of 0 can be represented as :: (only once per address)

### Transition from IPv4 to IPv6
1. Not all routers have IPv6
1. IPv6 packet is *tunneled* as payload in an IPv4 datagram
1. Transition to IPv6 has been really slow

## SDN

### Generalised Forwarding
1. Match of data in flow is done and then action is taken
1. Flow is given by in-link, network header, transport header

### OpenFlow
1. It is a communication protocol which determines the flow table entries
1. Match (ingress port, link layer, network layer, transport layer) $\to$ Action (forward, drop, modify, encapsulate + send to controller) $\to$ Stats (packet + byte counters)
1. Router is a subset of OpenFlow in a way hence
1. Similarly switch, NAT and firewall are also subsets
