package com.example.UnitTestKafka.kafka;

import com.example.UnitTestKafka.model.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class PersonKafkaConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(PersonKafkaConsumer.class);

    @KafkaListener(topics = "${spring.kafka.topic.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(@Payload Person person,
                        @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) Integer partition,
                        @Header(KafkaHeaders.OFFSET) Long offset) {
        LOGGER.info("Received a message contains a person information with id {}, from {} topic, " +
                "{} partition, and {} offset", person.getUuid(), topic, partition, offset);
    }
}
