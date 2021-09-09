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

# Lecture 1 (Background)

## Older Form of Communication - Telephone {#sec:telephone}
- Developed by Alexander Graham Bell
- Breakthrough was converting voice to electrical signals
- Telephone is technically not a network but an application

### How did Tele-Communication via Telephones happen?
- Multiple "nodes" present - clients are end nodes
- Metadata about the connection stored at each node on the route, hence reservation of resources needed
- If resources not available (client or a middle node), network is *busy*
- Since path fixed, destination address not sent during the conversation (only during initialisation), only a unique identifier to the call
- Information transmitted as continuous stream of data
- Source tears down the circuit when call ends

This approach is called **circuit switching**

### History of "Switches"
1. Manual human switch
2. Modern switch developed by Almon Brown Strowger ("girl-less, cuss-less") - electromechanical device

### Timing in Circuit Switching
The connection completion and destruction combined has a high overhead

### Sharing a link (multiplexing)
1. **Time Division Multiple Access (TDMA)**
    - Divide every "cycle" into n sub-cycles
    - Need to sync the sub-cycle (dynamically)
    - Capacity is lost when not all sub-cycles being used
2. **Frequency Division Multiple Access (FDMA)**: different frequencies for each "call"

### Strengths and Weaknesses
- **Predictable**: known delays but not drops
- **Easy to control**: centralised management
- **Not resilient to failures**:
    i. Failure of any node on the path prevents transmission (new routes are not created dynamically)
    i. Each transmission had a huge overhead
- **Wastes bandwidth**: peak bandwidth (P) larger than average bandwidth (A), A/P about 1/3 for a telephone call but 1/100 or lower for data transfer  
  Resolution: Using **Statistical Multiplexing**, this strategy aims to support about $2-3\times$ of average behaviour
- Optimised only for voice communication
- Setup time high

### Fixes and Ways to Overcome the Weaknesses
Paul Baran was one of the pioneers for improvements. His paper "On Distributed Communications" (1964) contained the following three points:

1. Distributed control
2. Message blocks (packets)
3. Store-and-forward delivery

The above points solved the problem of connectivity, however, to resolve the issue of wasted bandwidth, Len Kleinrock analysed packet switching and statistical multiplexing


## Taxonomy of Networks

Communication network can be classified into:

1. **Broadcast Communication Network**
2. **Switched Communication Network**

### Broadcast Communication Network
- Information transmitted by any node is heard by every other node on the network
- Limited range (usually of the order of LAN)
- Has issues of coordination (Multiple Access Problem) and privacy as well

### Switched Communication Network
1. **Circuit-Switched CN** (discussed [above](#telephone))
2. **Packet-Switched CN**

#### Packet-Switched Communication Network
- Each packet is made up of a header, data and (optionally) trailer
- **Routing** is determined dynamically using header information
- At each node, the packet is stored temporarily before it is forwarded to the next node (**store-and-forward**)

Packet-Switched is further divided into:

1. **Datagram Network**
2. **Virtual Circuit Network** - "Hybrid" of circuits and packets where a *virtual* circuit is established and communication then done in packets 

##### Datagram Network
- Each packet is independently switched and hence header contains complete destination address
- Routing decision is made independently for each packet
- Uses **statistical multiplexing**, will fail in the ideal worst-case when *all* packets arrive at the same time, assumes independence of traffic sources and computes the *expected* scenario

