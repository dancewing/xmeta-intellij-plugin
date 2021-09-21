package io.xmeta.jetbrains.database;

import io.xmeta.api.GraphDatabaseApi;
import io.xmeta.jetbrains.component.datasource.state.DataSource;

public interface DatabaseManagerService {

    GraphDatabaseApi getDatabaseFor(DataSource dataSource);
}
