package io.xmeta.jetbrains.database;

import io.xmeta.api.GraphDatabaseApi;
import io.xmeta.impl.OpenCypherGremlinDatabase;
import io.xmeta.jetbrains.component.datasource.state.DataSource;

public class DatabaseManagerServiceImpl implements DatabaseManagerService {

    public DatabaseManagerServiceImpl() {
    }

    public GraphDatabaseApi getDatabaseFor(DataSource dataSource) {
        switch (dataSource.getDataSourceType()) {
            case OPENCYPHER_GREMLIN:
                return new OpenCypherGremlinDatabase(dataSource.getConfiguration());
            default:
                throw new RuntimeException(String.format("Database for data source [%s] does not exists", dataSource.getDataSourceType()));
        }
    }
}
