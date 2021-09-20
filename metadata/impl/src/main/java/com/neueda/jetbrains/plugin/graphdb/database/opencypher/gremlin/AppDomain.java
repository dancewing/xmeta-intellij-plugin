package com.neueda.jetbrains.plugin.graphdb.database.opencypher.gremlin;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neueda.jetbrains.plugin.graphdb.database.api.data.App;
import com.neueda.jetbrains.plugin.graphdb.database.api.data.GraphEntity;

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
