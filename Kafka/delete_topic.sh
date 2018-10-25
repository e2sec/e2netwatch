source ./kafka.conf

TOPIC="$1"

[ -z "$TOPIC" ] && echo "ERROR: Argument <topic> is missing" && exit 1

echo "TOPIC: $TOPIC"

docker exec -it $CONTAINER /opt/kafka/bin/kafka-topics.sh \
    --zookeeper $ZOOKEEPER \
    --delete --topic $TOPIC