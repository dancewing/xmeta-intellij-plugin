package io.xmeta.jetbrains.configuration;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import io.xmeta.jetbrains.component.datasource.DataSourceType;
import io.xmeta.jetbrains.component.datasource.state.DataSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@State(name = "GraphDatabaseSupport.DataSourcesState",
        storages = {@Storage("GraphDatabaseSupport_DataSourcesState.xml")})
public class DataSourcesSettings implements PersistentStateComponent<DataSourcesSettings> {

    public List<DataSource> dataSources = new ArrayList<>();

    public DataSourcesSettings() {
    }

    @Nullable
    public static DataSourcesSettings getInstance(Project project) {
        return ServiceManager.getService(project, DataSourcesSettings.class);
    }

    @Override
    public void loadState(@NotNull DataSourcesSettings state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    /**
     * Get state for persisting.
     */
    @Nullable
    @Override
    public DataSourcesSettings getState() {
        return this;
    }

    public List<DataSource> getDataSources() {
        return dataSources;
    }


    public Optional<DataSource> findDataSource(String uuid) {
        return getDataSources().stream()
                .filter((dataSource) -> dataSource.getUUID().equals(uuid))
                .findFirst();
    }

    public void addDataSource(DataSource dataSource) {
        getDataSources().add(dataSource);
    }

    public void updateDataSource(DataSource oldDataSource, DataSource newDataSource) {
        int index = getDataSources().indexOf(oldDataSource);
        getDataSources().set(index, newDataSource);
    }

    public void removeDataSources(List<DataSource> dataSourcesForRemoval) {
        getDataSources().removeAll(dataSourcesForRemoval);
    }

    public Optional<DataSource> getDataSource(final String dataSourceName) {
        return getDataSources().stream()
                .filter((dataSource) -> dataSource.getName().equals(dataSourceName))
                .findAny();
    }

    public boolean isDataSourceExists(String dataSourceName) {
        return getDataSource(dataSourceName).isPresent();
    }

    public DataSource createDataSource(DataSource dataSourceToEdit, DataSourceType dataSourceType,
                                       String dataSourceName, Map<String, String> configuration) {
        String uuid = dataSourceToEdit == null ? UUID.randomUUID().toString() : dataSourceToEdit.getUUID();
        return new DataSource(uuid, dataSourceName, dataSourceType, configuration);
    }
}
