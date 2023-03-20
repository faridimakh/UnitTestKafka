# UnitTestKafka

## main:

this project conatain a simple producer and simple consumer writen spring boot framework.
you can send messges after starting the application (run UnitTestKafkaApplication) using postman (or comand line Curl), here an example: <br>
*curl --location --request POST 'http://localhost:8080/publish' --header 'Content-Type: application/json' --data-raw '{"uuid":"keyfar","firstName":"farid","lastName":"imakh","loc":{"lat":48.856613,"lgt":2.352222}}'* <br><br>

## test

the object in this project is to show to aproches how to testing this producer and consumer:
1. using embidded kafka tests: in this approach the Producer and the Consumer will be tested independently
2. using Kafka test containers: cette approch more simple, but need Docker installed in you server:
1. [x] pool kafka image (confluentinc/cp-kafka:6.1.0),
2. [x] send the a message to the topic using the producer (PersonKafkaProducer class),
3. [x] pool the message from the topic using the consumer (Persona KafkaConsumer class),
4. [x] check that the message values pooled by the consumer are the same as those sent.