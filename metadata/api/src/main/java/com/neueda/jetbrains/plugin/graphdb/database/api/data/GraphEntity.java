package com.neueda.jetbrains.plugin.graphdb.database.api.data;

import java.util.List;
import java.util.Map;

public interface GraphEntity extends NoIdGraphEntity, IDNameData {

    String getDisplayName();

    String getPluralDisplayName();

    Map<String, Object> getProperties();

    List<String> getTypes();

    String getTypesName();

    Integer getVersionNumber();

    List<GraphField> getFields();

}
