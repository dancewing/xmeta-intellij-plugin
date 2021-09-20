package com.neueda.jetbrains.plugin.graphdb.database.api.data;

import java.util.Map;

public interface GraphField extends IDNameData {

     String getPermanentId();

     String getDisplayName();

     String getDataType();

     Map<String, Object> getProperties();

     Boolean getRequired();

     Boolean getSearchable();

     String getDescription();

     Integer getPosition();

     Boolean getUnique();
}
