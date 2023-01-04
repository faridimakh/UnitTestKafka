package com.example.UnitTestKafka.kafka;


import com.example.UnitTestKafka.model.Loc;
import com.example.UnitTestKafka.model.Person;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.verification.VerificationMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:3241"})
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PersonKafkaConsumerTest {

    @Value("${spring.kafka.topic.name}")
    private String TOPIC_NAME;

    private Producer<String, String> producer;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Autowired
    private ObjectMapper objectMapper;

    @SpyBean
    private PersonKafkaConsumer personKafkaConsumer;


    @Captor
    ArgumentCaptor<ConsumerRecord<String, Person>> personArgumentCaptor;

    @Captor
    ArgumentCaptor<String> topicArgumentCaptor;

    @Captor
    ArgumentCaptor<Integer> partitionArgumentCaptor;

    @Captor
    ArgumentCaptor<Long> offsetArgumentCaptor;


    @BeforeAll
    void setUp() {
        Map<String, Object> configs = new HashMap<>(KafkaTestUtils.producerProps(embeddedKafkaBroker));
        producer = new DefaultKafkaProducerFactory<>(configs, new StringSerializer(), new StringSerializer()).createProducer();
    }

    @Test
    void testLogKafkaMessages() throws JsonProcessingException {
        // Write a message(person) to Kafka using a test producer
        Person pushed_person=new Person("farim", "farid", "imakh", new Loc(2.2414, 1.2155));
        String message = objectMapper.writeValueAsString(pushed_person);
        producer.send(new ProducerRecord<>(TOPIC_NAME, 0, "farim", message));
        producer.flush();

        // Read the message and assert its properties

        verify(personKafkaConsumer,timeout(1000).times(1)).consume(
                personArgumentCaptor.capture(),
                topicArgumentCaptor.capture(),
                partitionArgumentCaptor.capture(),
                offsetArgumentCaptor.capture());
        //capturing payload
        Person captured_person = personArgumentCaptor.getValue().value();
        //capturing key
        String captured_key = personArgumentCaptor.getValue().key();

        //check if the captured key and value are both not null:
        assertNotNull(captured_person);
        assertNotNull(captured_key);
        //test key pushed equal to key captured
        assertEquals(pushed_person.getUuid(),captured_key);
        //test payload pushed equal to payload captured

        assertEquals(pushed_person.getUuid(), captured_person.getUuid());
        assertEquals(pushed_person.getFirstName(), captured_person.getFirstName());
        assertEquals(pushed_person.getLastName(), captured_person.getLastName());
        assertEquals(pushed_person.getLoc().getLat(), captured_person.getLoc().getLat());
        assertEquals(pushed_person.getLoc().getLgt(), captured_person.getLoc().getLgt());

        assertEquals(TOPIC_NAME, topicArgumentCaptor.getValue());
        assertEquals(0, partitionArgumentCaptor.getValue());
        assertEquals(0, offsetArgumentCaptor.getValue());
    }

    @AfterAll
    void shutdown() {
        producer.close();
    }

}

