package com.example.UnitTestKafka.kafka;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
class KafkaProducerTest {
    String topic = "test_topic";

    @Autowired
    private KafkaProducer producer;


    @Autowired
    private KafkaConsumer consumer;

    @Test
    public void test_Producer_ok() throws Exception {
        String data = "messagetest";
        producer.send(topic, data);
        boolean messageConsumed = consumer.getLatch().await(10, TimeUnit.SECONDS);
        assertTrue(messageConsumed);
        assertThat(consumer.getPayload(), containsString(data));
    }

    @Test
    public void test_Producer_ko() throws Exception {
        String data = "messagetest";
        String data1 = "messagetest1";
        producer.send(topic, data);
        boolean messageConsumed = consumer.getLatch().await(0, TimeUnit.SECONDS);
        assertTrue(messageConsumed);
        assertThat(consumer.getPayload(), containsString(data1));
    }




}