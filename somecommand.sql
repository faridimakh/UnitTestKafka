kafka-topics --create --zookeeper zookeeper:2181 --topic topic_test --replication-factor 1 --partitions 1 --config "cleanup.policy=compact" --config min.cleanable.dirty.ratio=0.001 --config segment.ms=5000

kafka-topics --bootstrap-server localhost:9092 --describe --topic topic_test

kafka-topics --bootstrap-server localhost:9092 --delete --topic topic_test

kafka-topics --bootstrap-server localhost:9092 --list

kafka-run-class kafka.tools.GetOffsetShell --broker-list localhost:9092 --topic topic_test --time -1
kafka-console-consumer --topic topic_test --from-beginning --bootstrap-server localhost:9092


./kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic indexing-v1 --from-beginning  | jq
