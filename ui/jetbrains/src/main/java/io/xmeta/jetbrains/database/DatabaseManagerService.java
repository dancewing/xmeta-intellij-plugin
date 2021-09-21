package io.xmeta.jetbrains.database;

import io.xmeta.api.MetaDatabaseApi;
import io.xmeta.jetbrains.component.datasource.state.DataSource;

public interface DatabaseManagerService {

    MetaDatabaseApi getDatabaseFor(DataSource dataSource);
}
