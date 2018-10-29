#!/bin/bash

### add default targets "netflow" and "syslog" to Norikra for processing these streams ###

TARGET_SYSLOG="syslog  timestamp:string syslog_timestamp:string src_ip:string hostname:string appname:string loglevel:integer pid:integer message:string"

TARGET_NETFLOW="netflow  ipv4_dst_addr:string ipv4_src_addr:string l4_dst_port:integer l4_src_port:integer in_pkts:integer in_bytes:integer protocol:integer"

echo "adding target syslog..."
./norikra-client.sh target open $TARGET_SYSLOG

echo "adding target netflow..."
./norikra-client.sh target open $TARGET_NETFLOW
