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

# Lecture 13 (Control Place)

## Routing Protocols
Graph abstraction is performed with the links as nodes and cables as weighted edges

### Link State Algorithms
1. State of every link in the network is stored at each router
1. Dijkstra-like algorithms are performed
1. The algorithm is iterative and after $k$ iterations, least cost to $k$ nodes is known
1. Shortest path tree is computed
1. Cost complexity is $O(n^2)$ or $O(n\log{n})$
1. Message complexity (broadcasting edge weights and locations) is $O(n^2)$
1. Moving on the chosen path leads to oscillations since cost function changes on those routes
1. To resolve this, damping is done by using weighted function of queue length, RTT etc

### Distance Vector Algorithms
1. Link costs to only neighbours is known
1. Queries are made to neighbours to update the distances
1. Bellman-Ford algorithm is used
1. $k^{th}$ iteration updates distance using atmost $k$ hops
1. Algorithm waits for change in cost in neighbour, recomputes and notifies neighbours
