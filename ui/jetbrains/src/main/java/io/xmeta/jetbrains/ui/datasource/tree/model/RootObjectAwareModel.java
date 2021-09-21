package io.xmeta.jetbrains.ui.datasource.tree.model;

import io.xmeta.jetbrains.component.datasource.state.DataSource;
import io.xmeta.jetbrains.ui.datasource.metadata.dto.ContextMenu;
import io.xmeta.jetbrains.ui.datasource.tree.TreeNodeModelApi;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public abstract class RootObjectAwareModel implements TreeNodeModelApi {

    private DataSource dataSourceApi;
    private Object rootObject;

    public RootObjectAwareModel(DataSource dataSourceApi, Object rootObject) {
        this.dataSourceApi = dataSourceApi;
        this.rootObject = rootObject;
    }

    @Override
    public Optional<ContextMenu> getContextMenu() {
//        if (rootObject instanceof NoIdEntity) {
//            return Optional.of(new EntityContextMenu(dataSourceApi, (NoIdEntity) rootObject));
//        } else if (this instanceof NoIdEntity) {
//            return Optional.of(new EntityContextMenu(dataSourceApi, (NoIdEntity) this));
//        }
        return Optional.empty();
    }

    @Override
    public Optional<Object> getRootObjectValue() {
        return Optional.of(rootObject);
    }

    @Nullable
    @Override
    public DataSource getDataSourceApi() {
        return dataSourceApi;
    }
}
