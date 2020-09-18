package com.space.controller;

public enum ShipOrder {
    ID("id"), // default
    NAME("name"),
    PLANET("planet"),
    SHIP_TYPE("shipType"),
    DATE("prodDate"),
    IS_USED("isUsed"),
    SPEED("speed"),
    CREW_SIZE("crewSize"),
    RATING("rating");

    private String fieldName;

    ShipOrder(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}