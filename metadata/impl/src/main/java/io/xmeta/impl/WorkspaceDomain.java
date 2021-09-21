package io.xmeta.impl;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.xmeta.api.data.App;
import io.xmeta.api.data.Workspace;

import java.util.List;


public class WorkspaceDomain implements Workspace {
    private String id;
    private String name;
    private List<App> apps;

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

    @JsonDeserialize(contentAs = AppDomain.class)
    public List<App> getApps() {
        return apps;
    }

    public void setApps(List<App> apps) {
        this.apps = apps;
    }
}