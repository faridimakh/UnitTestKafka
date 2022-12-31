package com.example.UnitTestKafka.model;

public class Loc {
    private double lat;
    private double lgt;

    public Loc() {
    }

    public double getLat() {
        return lat;
    }

    public double getLgt() {
        return lgt;
    }

    public Loc(double lat, double lgt) {
        this.lat = lat;
        this.lgt = lgt;
    }

    @Override
    public String toString() {
        return "Loc{" +
                "lat=" + lat +
                ", lgt=" + lgt +
                '}';
    }
}
