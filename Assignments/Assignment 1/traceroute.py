#!/usr/bin/env python

import argparse
from collections import defaultdict
import matplotlib.pyplot as plt
import socket
import struct
import time

parser = argparse.ArgumentParser(description="Implement traceroute \
                                 and display the plot of the trace")
parser.add_argument('host', help="destination hostname (IP or URL)")
parser.add_argument('-f', dest='initial_hop', type=int, default=1,
                    help="set the initial hop distance")
parser.add_argument('-m', dest='max_hop', type=int, default=255,
                    help="set the maximal hop count (default 255)")
parser.add_argument('-p', dest='probe_count', type=int, default=3,
                    help="number of probes to be made for \
                    each hop (default 3)")
parser.add_argument('-t', dest='timeout', type=int, default=1,
                    help="max number of seconds to wait for \
                    each ping (default 1)")
parser.add_argument('-s', dest='file', help="store the plot in FILE")
args = parser.parse_args()

ICMP_ECHO_REQUEST = 8
ICMP_CODE = socket.getprotobyname('icmp')
sock = None
rtt = defaultdict(list)
try:
    DEST = socket.gethostbyname(args.host)
except socket.gaierror:
    print("error: invalid URL/IP address")
    exit(3)


def create_socket():
    global sock
    try:
        sock = socket.socket(socket.AF_INET, socket.SOCK_RAW, ICMP_CODE)
        sock.settimeout(args.timeout)
    except socket.error as e:
        print("error: could not create socket \
              (make sure you are running program as root)")
        print(e)
        exit(2)


# 1's complement checksum - computed by:
# segmenting the given string into 16 bit integers
# adding them and taking the 1's complement
# on the receiver's end, the checksum should equal 0
def checksum(msg):
    s = 0
    for i in range(0, len(msg), 2):
        s += msg[i] + (msg[i+1] << 8)
        s = (s & 0xffff) + (s >> 16)
    return socket.htons(~s & 0xffff)  # htons ensures that number in big-endian


def ping(ttl):
    # update the socket with fixed TTL
    try:
        sock.setsockopt(socket.SOL_IP, socket.IP_TTL, ttl)
    except socket.error:
        print("error: couldn't set TTL value")
        exit(4)

    # create a packet with no data (only header)
    packet = struct.pack("!BBHHH", ICMP_ECHO_REQUEST, 0, 0, 0, 0)
    packet = struct.pack("!BBHHH", ICMP_ECHO_REQUEST, 0,
                         checksum(packet), 0, 0)

    # make "probe_count" attempts to find the IP with the hop value enroute
    print(ttl, end='\t', flush=True)
    prev_addr = ""
    reached = False
    for _ in range(args.probe_count):
        try:
            t = time.time()
            sock.sendto(packet, (DEST, 1))
            response, (addr, _) = sock.recvfrom(1024)
            t = (time.time() - t) * 1000
            if checksum(response) != 0:
                raise socket.timeout
            code = struct.unpack("B", response[20:21])[0]
            rtt[ttl].append([t, addr, code])
            reached |= code == 0
            if addr != prev_addr:
                print("({})".format(addr), "%.2f" % rtt[ttl][-1][0], end='\t',
                      flush=True)
            else:
                print("%.2f" % rtt[ttl][-1][0], end='\t', flush=True)
            prev_addr = addr
        except socket.timeout:
            print('*', end='\t', flush=True)
    print()
    if prev_addr == "":
        rtt[ttl].append([0, prev_addr])
    return reached


def lambda_min(lst):
    try:
        return min(filter(lambda tpl: tpl[-1] == 0, lst))[0]
    except ValueError:
        return min(lst)[0]


if __name__ == '__main__':
    create_socket()
    ttl = args.initial_hop
    print("Traceroute starting for {} ({})".format(args.host, DEST))
    while ttl <= min(255, args.max_hop) and not ping(ttl):
        ttl += 1
    sock.close()

    times = list(map(lambda lst: lambda_min(lst), rtt.values()))
    diff = [times[0]]
    prev = diff[0]
    for i in range(1, len(times)):
        diff.append(max(0, (times[i] - prev) / 2))
        if times[i] != 0:
            prev = times[i]

    plt.plot(rtt.keys(), times, label="RTT")
    plt.plot(rtt.keys(), diff, label="time to next router")
    plt.xlabel("Hops")
    plt.ylabel("Time (ms)")
    plt.title("Plot of Hops vs Round Trip Time")
    plt.legend()
    if args.file:
        try:
            plt.savefig(args.file)
        except Exception:
            print("error: could not save plot")
    plt.show()
