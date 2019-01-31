{
  "aliases": {},
  "mappings": {
    "doc": {
      "properties": {
        "description": {
          "type": "text",
          "fields": {
            "keyword": {
              "type": "keyword",
              "ignore_above": 256
            }
          }
        },
        "destination": {
          "properties": {
            "ip": {
              "type": "ip",
              "fields": {
                "keyword": {
                  "type": "keyword",
                  "ignore_above": 256
                }
              }
            },
            "port": {
              "type": "long"
            }
          }
        },
        "host": {
          "properties": {
            "ip": {
              "type": "ip",
              "fields": {
                "keyword": {
                  "type": "keyword",
                  "ignore_above": 256
                }
              }
            }
          }
        },
        "netflow": {
          "properties": {
            "inbound": {
              "properties": {
                "first_switched": {
                  "type": "date"
                },
                "last_switched": {
                  "type": "date"
                },
                "records": {
                  "type": "long"
                },
                "sequence_number": {
                  "type": "long"
                }
              }
            },
            "outbound": {
              "properties": {
                "first_switched": {
                  "type": "date"
                },
                "last_switched": {
                  "type": "date"
                },
                "records": {
                  "type": "long"
                },
                "sequence_number": {
                  "type": "long"
                }
              }
            }
          }
        },
        "network": {
          "properties": {
            "iana_number": {
              "type": "long"
            },
            "inbound": {
              "properties": {
                "bytes": {
                  "type": "long"
                },
                "packets": {
                  "type": "long"
                }
              }
            },
            "outbound": {
              "properties": {
                "bytes": {
                  "type": "long"
                },
                "packets": {
                  "type": "long"
                }
              }
            }
          }
        },
        "source": {
          "properties": {
            "ip": {
              "type": "ip",
              "fields": {
                "keyword": {
                  "type": "keyword",
                  "ignore_above": 256
                }
              }
            },
            "port": {
              "type": "long"
            }
          }
        }
      }
    }
  }
}
