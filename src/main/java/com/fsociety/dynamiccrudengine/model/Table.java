package com.fsociety.dynamiccrudengine.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class Table {

    private String name;
    private PrimaryKey primaryKey;
    private List<Attribute> attributeList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PrimaryKey getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(PrimaryKey primaryKey) {
        this.primaryKey = primaryKey;
    }

    public List<Attribute> getAttributeList() {
        return attributeList;
    }

    public void setAttributeList(List<Attribute> attributeList) {
        this.attributeList = attributeList;
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
