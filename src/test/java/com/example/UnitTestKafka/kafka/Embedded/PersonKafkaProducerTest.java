package com.example.UnitTestKafka.kafka.Embedded;

import com.example.UnitTestKafka.kafka.PersonKafkaProducer;
import com.example.UnitTestKafka.model.Loc;
import com.example.UnitTestKafka.model.Person;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:3241"})
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PersonKafkaProducerTest {
    //give topic name: src/test/resources/application.properties
    @Value("${spring.kafka.topic.name}")
    private String TOPIC_NAME;
    //
    private BlockingQueue<ConsumerRecord<String, String>> records;

    private KafkaMessageListenerContainer<String, String> container;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Autowired
    private PersonKafkaProducer personKafkaProducer;

    @Autowired
    private ObjectMapper objectMapper;


    @BeforeAll
    void setUp() {
//        create instances of KafkaConsumer
        DefaultKafkaConsumerFactory<String, String> consumerFactory = new DefaultKafkaConsumerFactory<>(getConsumerProperties());
//        properties of a message listener container
        ContainerProperties containerProperties = new ContainerProperties(TOPIC_NAME);

//      create "message listener container"  listen for messages on the "topic_test" topic
        container = new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);
//
//      create new LinkedBlockingQueue:
        records = new LinkedBlockingQueue<>(1);
//      add message received in the LinkedBlockingQueue
        container.setupMessageListener((MessageListener<String, String>) records::add);

//      start the container:
        container.start();
        ContainerTestUtils.waitForAssignment(container, embeddedKafkaBroker.getPartitionsPerTopic());
        //or:
        //ContainerTestUtils.waitForAssignment(container, 1);
    }

    @Test
    void sendMessage() throws InterruptedException, JsonProcessingException {
        // write a message(person.uuid, person) to Kafka
        Person pushed_person = new Person("keyfar", "farid", "imakh", new Loc(2.2414, 2.2155));
        personKafkaProducer.sendMessage(pushed_person);

        // pull the message  from LinkedBlockingQueue
        ConsumerRecord<String, String> message = records.poll(500, TimeUnit.MILLISECONDS);
        //check that the message is not null
        assertNotNull(message);
        //check that the key pulled from LinkedBlockingQueue equal to the key pushed with tour producer
        assertEquals(pushed_person.getUuid(), message.key());
        //parceing the paload as person class
        Person pulled_person = objectMapper.readValue(message.value(), Person.class);
        //check that the parced payload is not nul
        assertNotNull(pulled_person);
        //testing payload values
        assertEquals(pushed_person.getUuid(), pulled_person.getUuid());
        assertEquals(pushed_person.getFirstName(), pulled_person.getFirstName());
        assertEquals(pushed_person.getLastName(), pulled_person.getLastName());
        assertEquals(pushed_person.getLoc().getLat(), pulled_person.getLoc().getLat());
        assertEquals(pushed_person.getLoc().getLgt(), pulled_person.getLoc().getLgt());
    }

    private Map<String, Object> getConsumerProperties() {
        return Map.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, embeddedKafkaBroker.getBrokersAsString(),
                ConsumerConfig.GROUP_ID_CONFIG, "consumer",
                ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true",
                ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "10",
                ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "60000",
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    }

    @AfterAll
    void tearDown() {
        container.stop();
    }

}

