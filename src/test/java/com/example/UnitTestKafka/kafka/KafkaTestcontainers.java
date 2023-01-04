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
            new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:6.1.0"));

    @Autowired
    PersonKafkaProducer personKafkaProducer;

    @Autowired
    PersonKafkaConsumer personKafkaConsumer;



    @Test
    void test_produce_consume() throws InterruptedException {
        System.out.println("*********************************************************************************************");
        System.out.println(kafkaContainer.getBootstrapServers());
        System.out.println(kafkaContainer.getExposedPorts());
//        send a message
        Person person = new Person("keyfar", "farid", "imakh", new Loc(2.2414, 2.2155));
        personKafkaProducer.sendMessage(person);

        Pair<String, Person> message = personKafkaConsumer.awaitMessage();

        Person message_payload = message.getValue1();
        String message_key = message.getValue0();

        Assertions.assertEquals(person.getUuid(), message_key);
        Assertions.assertEquals(person.getUuid(), message_payload.getUuid());
        Assertions.assertEquals(person.getFirstName(), message_payload.getFirstName());
        Assertions.assertEquals(person.getLastName(), message_payload.getLastName());
        Assertions.assertEquals(person.getLoc().getLat(), message_payload.getLoc().getLat());
        Assertions.assertEquals(person.getLoc().getLgt(), message_payload.getLoc().getLgt());
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
