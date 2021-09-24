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

# Lecture 0 (Introduction)

## Overview
1. This is an introductory undergraduate course on Computer Networks.
2. The focus will be on the suite of protocols used in the Internet.
3. The course will cover protocols at the Application, Transport, Network, Data link and Physical layers.

## Textbooks
- Computer Networking: A Top-Down Approach by Jim Kuorse and Keith Ross.
- Computer Networks: A Systems Approach by Larry Peterson and Bruce Davie.

## Course Evaluation
- Minor Exam: 25%
- Major Exam: 30%
- Assignments: 25% (individual)
- Quizzes: 15%
- Class participation: 5%  
(80% attendance mandatory)

### What is the Course About?
The course is about the **internet**, i.e., the packet data network

## Purpose of the Internet
1. Ability to connect to many different networks
2. Scale the entire globe
3. Be able to recover from failures
4. Basic goals:
	i. Speed
	i. Cost
	i. Reliability
	i. Port-density

## Internet Architecture vs Internet Engineering  
The architecture decides **what** tasks get done and **where** do they get do (protocol?), whereas the engineering decides **how** these tasks get done (implementation).  
For example, if resending of the packet was managed by the network rather than the sender (on failure), the network would need to store a lot of information regarding the packet until success.

## Topics to be Covered
1. Various aspects of internet architecture
	i. IP
	i. DNS
	i. BGP  
	**Routing**: Deciding the best way to reach destination from the source
		- Source routing (sender pre-decides the route)
		- *Node* routing (the nodes on the way decide the next node to send data to)
2. Higher-level protocols
	i. TCP
	i. HTTP(S)
3. Lower-level technologies
	i. Ethernet
	i. Wireless
	i. Topology

(Won't be covered: sensornets, low-level encoding, radio technology)

## Various Perspectives on Internet
1. Different levels of abstraction
2. Geographic scales: LAN, Enterprise, WAN
3. Various conceptual approaches: architecture, protocol, algorithm
4. Aspects of functionality: the *abstract* layers
	- email, WWW
	- SMTP, HTTP, RTP
	- TCP, UDP
	- IP
	- ethernet, PPP
	- CSMA, async, sonet
	- copper, fibre, radio  
	(abstraction increasing in bottom to top order)

## Basics
1. General overview - packet switching, basic design principles
2. Idealised view of network (ignoring *friction, air drag*) to answer fundamental questions:
    i. How to deliver packet from source to destination
    i. Building reliable transport on an unreliable network
    i. Federate multiple ISPs
3. Real view - IP, TCP, DNS, web

#### What would you choose?
Internet without the modern computer, or modern computer without internet?

## Challenges  
(mostly because updates/upgrades will affect a very huge popluation, rolling out difficult too)

1. Security
2. Availability
3. Evolution

## Second Half of Course
1. Congestion control
2. Advanced topics in routing
3. Multicast and QoS
4. Security
5. Ethernet
6. Wireless
7. Software-defined networking
8. Alternate architectures

