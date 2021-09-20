package com.neueda.jetbrains.plugin.graphdb.database.api.data;

import java.util.List;

public interface Workspace extends IDNameData {
    List<App> getApps();
}
