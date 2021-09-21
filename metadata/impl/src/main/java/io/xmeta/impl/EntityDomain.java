package io.xmeta.impl;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.xmeta.api.data.GraphEntity;
import io.xmeta.api.data.GraphField;

import java.util.List;
import java.util.Map;

public class EntityDomain implements GraphEntity {

    private String id;
    private String name;
    private String displayName;
    private String pluralDisplayName;
    private Integer versionNumber;
    private List<GraphField> fields;

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

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPluralDisplayName() {
        return pluralDisplayName;
    }

    public void setPluralDisplayName(String pluralDisplayName) {
        this.pluralDisplayName = pluralDisplayName;
    }

    @JsonDeserialize(contentAs = FieldDomain.class)
    public List<GraphField> getFields() {
        return fields;
    }

    public void setFields(List<GraphField> fields) {
        this.fields = fields;
    }

    public Integer getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(Integer versionNumber) {
        this.versionNumber = versionNumber;
    }

    @Override
    public Map<String, Object> getProperties() {
        return null;
    }

    @Override
    public List<String> getTypes() {
        return null;
    }

    @Override
    public String getTypesName() {
        return null;
    }

    @Override
    public boolean isTypesSingle() {
        return false;
    }

    @Override
    public String getRepresentation() {
        return null;
    }
}
