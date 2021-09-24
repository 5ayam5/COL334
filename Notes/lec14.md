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

# Lecture 14 (Analysis of Distance Vector)

## Advantages and Drawbacks
1. Good news travels fast and bad news travels slow
1. It is possible that when a link cost increases, the same is propagated slowly since the *best path* may travel from the changed link
1. This makes the update for *bad news* slow (count-to-infinity problem)
1. The issue can be fixed using *path vector*

## Comparison of LS with DV
1. The DV algorithm is faster in message and convergence complexity (usually)
1. DV algorithm can be exploited by a router advertising incorrect path cost
1. This can lead to the data being "snooped" by the malicious router
1. This issue doesn't exist in LS algorithm hence it is more secure

## Approach to Scalable Routing
1. Autonomous Systems (AS) are "registered"
1. All routers within an AS run the same intra-domain protocol for routing
1. *Gateway routers* need to run two protocols - intra and inter domain protocols
