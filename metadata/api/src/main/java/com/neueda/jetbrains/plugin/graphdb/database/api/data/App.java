package com.neueda.jetbrains.plugin.graphdb.database.api.data;

import java.util.List;

public interface App extends IDNameData {
    List<GraphEntity> getEntities();
}
