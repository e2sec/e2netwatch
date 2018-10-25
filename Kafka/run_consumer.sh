source ./kafka.conf

TOPIC="$1"

[ -z "$TOPIC" ] && echo "ERROR: Argument <topic> is missing" && exit 1

docker exec -it $CONTAINER /opt/kafka/bin/kafka-console-consumer.sh --bootstrap-server $BOOTSTRAP_SERVER --topic $TOPIC
