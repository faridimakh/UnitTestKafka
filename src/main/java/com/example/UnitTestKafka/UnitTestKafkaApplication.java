package com.example.UnitTestKafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UnitTestKafkaApplication {

	public static void main(String[] args) {
		SpringApplication.run(UnitTestKafkaApplication.class, args);
	}

}
//valid data exampl to send as http post request:
//{"uuid":"keyfar","firstName":"farid","lastName":"imakh","loc":{"lat":48.856613,"lgt":2.352222}}
//with curl:
//curl --location --request POST 'http://localhost:8080/publish' --header 'Content-Type: application/json' --data-raw '{"uuid":"keyfar","firstName":"farid","lastName":"imakh","loc":{"lat":48.856613,"lgt":2.352222}}'