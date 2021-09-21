package io.xmeta.jetbrains.ui.datasource.tree;

import io.xmeta.api.data.IDNameData;
import io.xmeta.jetbrains.component.datasource.state.DataSource;
import io.xmeta.jetbrains.ui.datasource.metadata.dto.ContextMenu;
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
