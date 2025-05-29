To create kafka topic inside docker 

docker exec -it broker /opt/kafka/bin/kafka-topics.sh --create --topic messages.in-review --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1

To run elastic search in docker container

docker run -p 9200:9200 -e "discovery.type=single-node" elasticsearch:7.17.22
