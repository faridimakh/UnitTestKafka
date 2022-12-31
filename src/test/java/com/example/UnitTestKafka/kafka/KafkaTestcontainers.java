package com.example.UnitTestKafka.kafka;

import com.example.UnitTestKafka.model.Loc;
import com.example.UnitTestKafka.model.Person;
import org.javatuples.Pair;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest
@Testcontainers
public class KafkaTestcontainers {
    @Container
    public static KafkaContainer kafkaContainer =
            new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka"));

    @Autowired
    PersonKafkaProducer personKafkaProducer;

    @Autowired
    PersonKafkaConsumer personKafkaConsumer;

    @Test
    void test_produce_consume() throws InterruptedException {
//        send a message
        Person person = new Person("keyfar", "farid", "imakh", new Loc(2.2414, 2.2155));
        personKafkaProducer.sendMessage(person);

        Pair<String, Person> message = personKafkaConsumer.awaitPayload();

        Person message_payload = message.getValue1();
        String message_key = message.getValue0();
        Assertions.assertEquals("keyfar", message_key);
        Assertions.assertEquals("keyfar", message_payload.getUuid());
        Assertions.assertEquals("farid", message_payload.getFirstName());
        Assertions.assertEquals("imakh", message_payload.getLastName());
        Assertions.assertEquals(2.2414, message_payload.getLoc().getLat());
        Assertions.assertEquals(2.2155, message_payload.getLoc().getLgt());
//        kafkaContainer.stop();
    }

    @DynamicPropertySource
    static void fetchBootstrapServersProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", () -> kafkaContainer.getBootstrapServers());
    }

    @AfterAll
    static void stopContainer() {
        kafkaContainer.stop();
    }

}
