---
geometry:
- top=25mm
- left=20mm
- right=20mm
- bottom=30mm
documentclass: extarticle
fontsize: 12pt
numbersections: true
title: Lecture 24 (Network Security)
---

# What is Network Security
1. Confidentiality
1. Authentication
1. Message integrity
1. Access and availability

# Malicious Actions
1. Eavesdropping
1. Impersonation
1. Hijacking
1. Denial of service

# Basic Idea of Cryptography
Alice uses $K_A$ and sends encrypted message to Bob who uses $K_B$ and decodes the message.
$$m = K_B(K_A(m))$$

## Types of Cryptography
1. Symmetric key - $K_A = K_B = K_S$
  - One issue is on how do they agree on the key
1. Public key
  - Public encryption key is known to all
  - Private decryption key is known only to receiver

### Symmetric Key Crypto - DES
1. Data Encryption Standard
1. Block cipher with cipher block chaining - messy implementation and not discussed in detail
1. 56-bit symmetric key and 64-bit plaintext input
1. Brute force decryption is possible within a day

### Advanced Encryption Standard - AES
1. Data is processed in 128 bit blocks
1. 128, 192 or 256 bit keys are used
1. Brute force for each key takes 1s on DES and 149 trillion years for AES

### Public Key Crypto
1. It should be impossible to compute private key
1. RSA - Rivest, Shamir, Adelson algorithm

#### RSA
1. Choose two large prime numbers $p, q$ (having 1024 bits each)
1. Compute $n = pq, z = (p-1)(q-1)$
1. Choose $e (< n)$ with no common factors with $z$
1. Choose $d$ such that $ed-1$ is divisible by $z$
1. Public key is $(n, e)$ and private key is $(n, d)
1. $c = m^e \mod{n}$
1. $m = c^d \mod{n}$
1. We can swap the public and private keys as well

**RSA Drawbacks**

1. Computation is slow since we need exponentiation of large numbers
1. Therefore, once RSA session is established, symmetric key is shared and encryption is then done using this key which is faster in computation


# Authentication
1. When a device claims to be Alice, Bob sends a number **nonce** (n-once-in-a-lifetime)
1. Now, Alice sends it back after encrypting it with her private key
1. To verify the same, Bob can now decrypt it using her public key
1. This prevents impersonation but man-in-the-middle can still happen
