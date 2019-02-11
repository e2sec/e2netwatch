{
  "order": 0,
  "version": 1,
  "index_patterns": [
    "netflow-*"
  ],
  "mappings": {
    "_default_": {
      "properties": {
        "@timestamp": {
          "type": "date"
        },
        "@version": {
          "type": "text",
          "fields": {
            "keyword": {
              "type": "keyword",
              "ignore_above": 256
            }
          }
        },
        "host": {
          "type": "ip"
        },
        "netflow": {
          "properties": {
            "dst_as": {
              "type": "long"
            },
            "dst_mask": {
              "type": "long"
            },
            "engine_id": {
              "type": "long"
            },
            "engine_type": {
              "type": "long"
            },
            "first_switched": {
              "type": "date"
            },
            "flow_records": {
              "type": "long"
            },
            "flow_seq_num": {
              "type": "long"
            },
            "in_bytes": {
              "type": "long"
            },
            "in_pkts": {
              "type": "long"
            },
            "input_snmp": {
              "type": "long"
            },
            "ipv4_dst_addr": {
              "type": "ip"
            },
            "ipv4_next_hop": {
              "type": "ip"
            },
            "ipv4_src_addr": {
              "type": "ip"
            },
            "l4_dst_port": {
              "type": "long"
            },
            "l4_src_port": {
              "type": "long"
            },
            "last_switched": {
              "type": "date"
            },
            "output_snmp": {
              "type": "long"
            },
            "protocol": {
              "type": "long"
            },
            "sampling_algorithm": {
              "type": "long"
            },
            "sampling_interval": {
              "type": "long"
            },
            "src_as": {
              "type": "long"
            },
            "src_mask": {
              "type": "long"
            },
            "src_tos": {
              "type": "long"
            },
            "tcp_flags": {
              "type": "long"
            },
            "version": {
              "type": "long"
            }
          }
        }
      }
    }
  },
  "settings": {
    "index": {
      "number_of_shards": "1",
      "number_of_replicas": "1"
    }
  }
}
