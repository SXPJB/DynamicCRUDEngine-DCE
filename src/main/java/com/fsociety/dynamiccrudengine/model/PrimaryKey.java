package com.fsociety.dynamiccrudengine.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PrimaryKey {
    private String name;
    private String type;
    private Boolean isAutoincrement;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getAutoincrement() {
        return isAutoincrement;
    }

    public void setAutoincrement(Boolean autoincrement) {
        isAutoincrement = autoincrement;
    }
    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return e.getMessage();
        }
    }
}
