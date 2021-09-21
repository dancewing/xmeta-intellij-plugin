package io.xmeta.impl;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.xmeta.api.data.App;
import io.xmeta.api.data.GraphEntity;

import java.util.List;

public class AppDomain implements App {
    private String id;
    private String name;
    private List<GraphEntity> entities;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonDeserialize(contentAs = EntityDomain.class)
    public List<GraphEntity> getEntities() {
        return entities;
    }

    public void setEntities(List<GraphEntity> entities) {
        this.entities = entities;
    }
}
