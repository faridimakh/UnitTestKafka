package com.example.UnitTestKafka.model;


public class Person {

    private String uuid;

    private String firstName;

    private String lastName;
    private Loc loc;


    public Person() {

    }

    public Person(String uuid, String firstName, String lastName, Loc loc) {
        this.uuid = uuid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.loc = loc;
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

    public Loc getLoc() {
        return loc;
    }

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
