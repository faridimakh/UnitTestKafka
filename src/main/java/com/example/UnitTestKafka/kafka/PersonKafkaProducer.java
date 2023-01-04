package com.example.UnitTestKafka.kafka;


import com.example.UnitTestKafka.model.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
@Service
public class PersonKafkaProducer {

    @Value("${spring.kafka.topic.name}")
    private String topic;

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonKafkaProducer.class);

    private final KafkaTemplate<String, Person> kafkaTemplate;

    public PersonKafkaProducer(KafkaTemplate<String, Person> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(Person data){

        LOGGER.info(String.format("Message sent -> %s", data));

        Message<Person> message = MessageBuilder
                .withPayload(data)
                .setHeader(KafkaHeaders.TOPIC, topic)
                .setHeader(KafkaHeaders.MESSAGE_KEY, data.getUuid())
                .build();

        kafkaTemplate.send(message);
    }
}


//message example to send:
