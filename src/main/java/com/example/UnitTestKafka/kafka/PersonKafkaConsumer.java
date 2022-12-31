

package com.example.UnitTestKafka.kafka;

import com.example.UnitTestKafka.model.Person;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.javatuples.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Service
public class PersonKafkaConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(PersonKafkaConsumer.class);
    private CountDownLatch latch = new CountDownLatch(1);
    private Person payload;
    private String key;

    @KafkaListener(topics = "${spring.kafka.topic.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(ConsumerRecord<String, Person> consumerRecord,
                        @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) Integer partition,
                        @Header(KafkaHeaders.OFFSET) Long offset) {
        this.key = consumerRecord.key();
        this.payload = consumerRecord.value();
        latch.countDown();
        LOGGER.info("Received a message contains a person information with id {}, from {} topic, " +
                "{} partition, and {} offset", consumerRecord.value().toString(), topic, partition, offset);
    }


    public Pair<String, Person> awaitPayload() throws InterruptedException {
        boolean messageConsumed = this.latch.await(500, TimeUnit.MILLISECONDS);
        if (!messageConsumed) {
            throw new RuntimeException("message not consumed");
        } else {
            this.latch = new CountDownLatch(1);
            return new Pair<>(this.key, this.payload);
        }
    }

}

