#!/usr/bin/env python3

import socket
import sys
import struct

if len(sys.argv) != 2:
    print("error: usage: ./traceroute.py [host name]")
    exit(1)

TAB = '\t'.expandtabs(4)

ICMP_ECHO_REQUEST = 8
ICMP_CODE = socket.getprotobyname('icmp')

try:
    DEST = socket.gethostbyname(sys.argv[1])
except socket.gaierror:
    print("error: invalid URL/IP address")
    exit(3)

# 1's complement checksum
def checksum(msg):
    s = 0
    for i in range(0, len(msg), 2):
        s += msg[i] + (msg[i+1] << 8)
        s = (s & 0xffff) + (s >> 16)
    return socket.htons(~s & 0xffff) # changing from big-endian to little endian

def ping(ttl):
    # create the socket with fixed TTL
    try:
        s = socket.socket(socket.AF_INET, socket.SOCK_RAW, ICMP_CODE)
        s.setsockopt(socket.SOL_IP, socket.IP_TTL, ttl)
        s.settimeout(1)
    except socket.error as e:
        print("error: could not create socket (make sure you are running the program as root)")
        print(e)
        exit(2)

    # create a packet with no data (only header)
    packet = struct.pack("!BBHHH", ICMP_ECHO_REQUEST, 0, 0, 0, 0)
    packet = struct.pack("!BBHHH", ICMP_ECHO_REQUEST, 0, checksum(packet), 0, 0)

    # make 3 attempts to find the IP with the hop value enroute
    print(ttl, end=TAB, flush=True)
    for _ in range(3):
        try:
            s.sendto(packet, (DEST, 1))
            _, addr = s.recvfrom(1024)
            print(addr[0])
            s.close()
            return addr[0]
        except socket.timeout:
            print('*', end=TAB, flush=True)
    print()
    s.close()
    return '*'

if __name__ == '__main__':
    ttl = 1
    print("Traceroute starting for {} ({})".format(sys.argv[1], DEST))
    while ping(ttl) != DEST:
        ttl += 1
