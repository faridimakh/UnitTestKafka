package com.example.UnitTestKafka.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Person {

    private String uuid;

    private String firstName;

    private String lastName;

    private Loc loc;

    @Override
    public String toString() {
        return "Person{" +
                "uuid='" + uuid + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", loc=" + loc +
                '}';
    }
}
