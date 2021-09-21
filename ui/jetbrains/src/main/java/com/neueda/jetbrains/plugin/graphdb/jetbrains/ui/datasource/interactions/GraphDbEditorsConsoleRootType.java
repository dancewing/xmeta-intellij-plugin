package com.neueda.jetbrains.plugin.graphdb.jetbrains.ui.datasource.interactions;

import com.intellij.execution.console.ConsoleRootType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.neueda.jetbrains.plugin.graphdb.jetbrains.component.datasource.DataSourceDescription;
import com.neueda.jetbrains.plugin.graphdb.jetbrains.component.datasource.state.DataSource;
import com.neueda.jetbrains.plugin.graphdb.jetbrains.configuration.DataSourcesSettings;
import com.neueda.jetbrains.plugin.graphdb.jetbrains.util.NameUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Optional;

public class GraphDbEditorsConsoleRootType extends ConsoleRootType {

    @NotNull
    public static GraphDbEditorsConsoleRootType getInstance() {
        return findByClass(GraphDbEditorsConsoleRootType.class);
    }

    GraphDbEditorsConsoleRootType() {
        super("graphdb-editors", "XMeta Data Source editor");
    }

    @Nullable
    @Override
    public Icon substituteIcon(@NotNull Project project, @NotNull VirtualFile file) {
        return getDataSource(project, file)
                .map(DataSource::getDescription)
                .map(DataSourceDescription::getIcon)
                .orElse(null);
    }

    @Nullable
    @Override
    public String substituteName(@NotNull Project project, @NotNull VirtualFile file) {
        return getDataSource(project, file)
                .map(DataSource::getName)
                .orElse(null);
    }

    private Optional<DataSource> getDataSource(@NotNull Project project, @NotNull VirtualFile file) {
        if (file.isDirectory()) {
            return Optional.empty();
        }

        DataSourcesSettings component = DataSourcesSettings.getInstance(project);
        return component.findDataSource(NameUtil.extractDataSourceUUID(file.getName()));
    }
}
