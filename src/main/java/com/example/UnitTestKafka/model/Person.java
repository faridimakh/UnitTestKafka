package com.example.UnitTestKafka.model;


public class Person {

    private String uuid;

    private String firstName;

    private String lastName;

    public Person() {

    }

    public Person(String uuid, String firstName, String lastName) {
        this.uuid = uuid;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getUuid() {
        return uuid;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
