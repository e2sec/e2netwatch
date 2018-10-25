source ./kafka.conf

TOPIC="$1"

PARTITIONS=${2:-"1"}

REPLICATION_FACTOR=${3:-"1"}

[ -z "$TOPIC" ] && echo "ERROR: Argument <topic> is missing" && exit 1

echo "TOPIC: $TOPIC"
echo "PARTITIONS: $PARTITIONS"
echo "REPLICATION_FACTOR: $REPLICATION_FACTOR"

docker exec -it $CONTAINER /opt/kafka/bin/kafka-topics.sh \
    --zookeeper $ZOOKEEPER \
    --create --topic $TOPIC \
    --partitions $PARTITIONS \
    --replication-factor $REPLICATION_FACTOR
