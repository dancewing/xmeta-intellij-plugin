package io.xmeta.impl;

import io.xmeta.api.data.EntityField;

import java.util.Map;

public class FieldDomain implements EntityField {
    private String id;
    private String permanentId;
    private String name;
    private String displayName;
    private String dataType;
    private Map<String, Object> properties;
    private Boolean required;
    private Boolean searchable;
    private String description;
    private Integer position;
    private Boolean unique;

    public void setId(String id) {
        this.id = id;
    }

    public void setPermanentId(String permanentId) {
        this.permanentId = permanentId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public void setSearchable(Boolean searchable) {
        this.searchable = searchable;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public void setUnique(Boolean unique) {
        this.unique = unique;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getPermanentId() {
        return permanentId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String getDataType() {
        return dataType;
    }

    @Override
    public Map<String, Object> getProperties() {
        return properties;
    }

    @Override
    public Boolean getRequired() {
        return required;
    }

    @Override
    public Boolean getSearchable() {
        return searchable;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Integer getPosition() {
        return position;
    }

    @Override
    public Boolean getUnique() {
        return unique;
    }
}
