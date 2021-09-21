package io.xmeta.impl;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.xmeta.api.data.MetaApp;
import io.xmeta.api.data.MetaEntity;

import java.util.List;

public class MetaAppDomain implements MetaApp {
    private String id;
    private String name;
    private List<MetaEntity> entities;

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
    public List<MetaEntity> getEntities() {
        return entities;
    }

    public void setEntities(List<MetaEntity> entities) {
        this.entities = entities;
    }
}
