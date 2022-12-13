kafka-topics --create --zookeeper zookeeper:2181 --topic db-topic --replication-factor 1 --partitions 1 --config "cleanup.policy=compact" --config min.cleanable.dirty.ratio=0.001 --config segment.ms=5000

kafka-topics --bootstrap-server localhost:9092 --describe --topic db-topic

kafka-topics --bootstrap-server localhost:9092 --delete --topic db-topic

kafka-topics --bootstrap-server localhost:9092 --list

kafka-run-class kafka.tools.GetOffsetShell --broker-list localhost:9092 --topic db-topic --time -1
kafka-run-class kafka.tools.GetOffsetShell --broker-list localhost:9092 --topic wikichangesresponses-edit --time -1
kafka-run-class kafka.tools.GetOffsetShell --broker-list localhost:9092 --topic wikichangesresponses-log --time -1
kafka-run-class kafka.tools.GetOffsetShell --broker-list localhost:9092 --topic wikichangesresponses-new --time -1
kafka-run-class kafka.tools.GetOffsetShell --broker-list localhost:9092 --topic wikichangesresponses-categorize --time -1
kafka-console-consumer --topic user --from-beginning --bootstrap-server localhost:9092


./kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic indexing-v1 --from-beginning  | jq
