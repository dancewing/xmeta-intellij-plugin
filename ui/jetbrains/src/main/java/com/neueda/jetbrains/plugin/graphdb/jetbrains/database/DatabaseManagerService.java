package com.neueda.jetbrains.plugin.graphdb.jetbrains.database;

import com.neueda.jetbrains.plugin.graphdb.database.api.GraphDatabaseApi;
import com.neueda.jetbrains.plugin.graphdb.jetbrains.component.datasource.state.DataSource;

public interface DatabaseManagerService {

    GraphDatabaseApi getDatabaseFor(DataSource dataSource);
}
