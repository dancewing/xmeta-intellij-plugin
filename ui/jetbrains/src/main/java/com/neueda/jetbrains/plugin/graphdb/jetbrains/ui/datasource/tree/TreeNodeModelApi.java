package com.neueda.jetbrains.plugin.graphdb.jetbrains.ui.datasource.tree;

import com.neueda.jetbrains.plugin.graphdb.database.api.data.IDNameData;
import com.neueda.jetbrains.plugin.graphdb.jetbrains.component.datasource.state.DataSource;
import com.neueda.jetbrains.plugin.graphdb.jetbrains.ui.datasource.metadata.dto.ContextMenu;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Optional;

public interface TreeNodeModelApi<T extends IDNameData> {

    NodeType getType();

    default Optional<ContextMenu> getContextMenu() {
        return Optional.empty();
    }

    default Optional<Icon> getIcon() {
        return Optional.empty();
    }

    default Optional<String> getText() {
        return Optional.empty();
    }

    default Optional<String> getDescription() {
        return Optional.empty();
    }

    default Optional<T> getValue() {
        return Optional.empty();
    }

    default Optional<T> getRootObjectValue() {
        return Optional.empty();
    }

    @Nullable
    DataSource getDataSourceApi();
}
