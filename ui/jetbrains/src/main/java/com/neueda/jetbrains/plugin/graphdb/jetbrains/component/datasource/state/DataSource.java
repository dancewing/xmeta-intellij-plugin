package com.neueda.jetbrains.plugin.graphdb.jetbrains.component.datasource.state;

import com.neueda.jetbrains.plugin.graphdb.jetbrains.component.datasource.DataSourceDescription;
import com.neueda.jetbrains.plugin.graphdb.jetbrains.component.datasource.DataSourceType;

import java.util.HashMap;
import java.util.Map;

/**
 * Legacy DataSource. Versioning schema was not invented when this class was created.
 */
public final class DataSource {

    public DataSourceType dataSourceType = DataSourceType.UNKNOWN;
    public String name = "unknown";
    private String UUID;
    public Map<String, String> configuration = new HashMap<>();

    /**
     * Default constructor for serialization.
     */
    public DataSource() {
    }

    public DataSource(String uuid, String name, DataSourceType dataSourceType,
                      Map<String, String> configuration) {
        this.UUID = uuid;
        this.dataSourceType = dataSourceType;
        this.name = name;
        this.configuration = configuration;
    }

    public DataSourceType getDataSourceType() {
        return dataSourceType;
    }

    public void setDataSourceType(DataSourceType dataSourceType) {
        this.dataSourceType = dataSourceType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public void setConfiguration(Map<String, String> configuration) {
        this.configuration = configuration;
    }

    public String getUUID() {
        return this.UUID;
    }

    public String getName() {
        return name;
    }

    public Map<String, String> getConfiguration() {
        return configuration;
    }

    public DataSourceDescription getDescription() {
        switch (getDataSourceType()) {
            case NEO4J_BOLT:
                return DataSourceDescription.NEO4J_BOLT;
            case OPENCYPHER_GREMLIN:
                return DataSourceDescription.OPENCYPHER_GREMLIN;
            default:
                throw new IllegalStateException("Unknown data source type encountered: " + getDataSourceType());
        }
    }
}
