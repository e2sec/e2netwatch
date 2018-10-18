source ./kafka.conf

docker exec -it $CONTAINER /opt/kafka/bin/kafka-topics.sh --zookeeper $ZOOKEEPER --list
