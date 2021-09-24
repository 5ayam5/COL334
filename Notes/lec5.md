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

# Lecture 5 (Application Layer pt 3)

## P2P Architecture
Compared to client-server architecture, file sharing time is reduced by huge amounts since each peer uploads the file to the other peers. BitTorrent is an example of a P2P network

### BitTorrent
1. The file is divided into chunks of size 256 kB
2. Peer asks for list of chunks from each peer periodically
3. Missing chunks are reuqested in order of rarity
4. Chunks are sent to top 4 peers who send chunks at highest rate (re-evaluated every 10 seconds)
5. Every 30s, another peer is randomly selected and chunks are sent to that peer

## Video Streaming and CDNs
Challenges that exist are the large consumer base and different bandwidths and speeds for every user

### Video
Video is made up of frames which are made up of pixels. Encoding of these frames is done more efficiently than pixel-by-pixel using different encoding techniques (spatial and temporal optimisations). Encoding also happens at different bit rates:

1. Constant Bit Rate (CBR)
2. Variable Bit Rate (VBR)

### Dynamic Adaptive Streaming over HTTP (DASH)
- Server
    1. The server divides the video into multiple chunks
    2. Each chunk is encoded at multiple different bitrates
    3. Each of the encoded version is stored in different files and replicated in various CDN nodes
    4. *Manifest file* provides URL for different chunks
- Client
    1. Estimates bandwidth periodically
    2. Consulting manifest, requests chunk by chunk
    3. Chooses maximum bitrate which is sustainable at current bandwidth
    4. Decided *when*, *what* and *where* to request

**streaming == encoding + DASH + playout buffering**

### Content Distribution Networks (CDNs)
Two methods are employed by CDN services:

1. Enter deep (large number of servers into many access networks)
2. Bring home (lesser servers in large clusters at PoPs [Points of Presence])

#### Working of CDNs
1. Client requests for manifest file from actual server
2. Manifest file returned by server
3. Using manifest, client retrieves content using the (DASH) strategy discussed above

