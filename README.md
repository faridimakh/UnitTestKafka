# UnitTestKafka
this project conatain a simple producer and simple consumer writen spring boot framework.
the object in this project is to show to aproches how to testing this producer and consumer:
1. using embidded kafka tests: in this approach the Producer and the Consumer will be tested independently
2. using Kafka test containers: cette approch more simple, but need Docker installed in you server:
- pool kafka image (confluentinc/cp-kafka:6.1.0),
- send the a message to the topic using the producer (PersonKafkaProducer class),
- pool the message from the topic using the consumer (Persona KafkaConsumer class),
- check that the message values pooled by the consumer are the same as those sent