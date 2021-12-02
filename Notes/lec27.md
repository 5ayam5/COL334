---
geometry:
- top=25mm
- left=20mm
- right=20mm
- bottom=30mm
documentclass: extarticle
fontsize: 12pt
numbersections: true
title: Lecture 27 (IKE)
--- 

# IKE
Authentication can happen using a Pre-Shared Key (PSK) or using PKI

## PSK
Both sides start with secret and then run IKE to authenticate each other and generate IPsec SAs

## PKI
1. Both sides start with public/private key pair and certificate
1. Run IKE to authenticate each other and obtain IPsec SAs
1. Similar to handshake in SSL


