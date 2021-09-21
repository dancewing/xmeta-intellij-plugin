package io.xmeta.impl;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.xmeta.api.data.MetaApp;
import io.xmeta.api.data.MetaWorkspace;

import java.util.List;


public class MetaWorkspaceDomain implements MetaWorkspace {
    private String id;
    private String name;
    private List<MetaApp> metaApps;

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

    @JsonDeserialize(contentAs = MetaAppDomain.class)
    public List<MetaApp> getApps() {
        return metaApps;
    }

    public void setApps(List<MetaApp> metaApps) {
        this.metaApps = metaApps;
    }
}
