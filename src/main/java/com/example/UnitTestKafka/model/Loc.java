package com.example.UnitTestKafka.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Loc {
    private double lat;
    private double lgt;

    @Override
    public String toString() {
        return "Loc{" +
                "lat=" + lat +
                ", lgt=" + lgt +
                '}';
    }
}
