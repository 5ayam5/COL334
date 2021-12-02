---
geometry:
- top=25mm
- left=20mm
- right=20mm
- bottom=30mm
documentclass: extarticle
fontsize: 12pt
numbersections: true
title: Lecture 25 (Public Key)
--- 

# Digital Signatures
1. Sender digitally signs document
1. The message is verifiable and nonforgeable
1. The signing is done using the private key
1. The digitally signed message is also non-repudiate-able

# Message Digests
1. Digital fingerprint is created by creating a fixed length hash of the message
1. This hash is then digitally signed instead of signing the entire message which might be long and hence slow

## Hash Functions in Use
1. MD5 hash function - 128-bit hash
1. SHA-1

# Preventing Man in the Middle Attacks
1. We need a centralised system to store public keys so that man in the middle attacks are not possible
1. Certification Authorities (CA) bind public key to particular entity
1. Bob's public key and identifying info are encrypted using CA's private key and stored as the digital signature 
