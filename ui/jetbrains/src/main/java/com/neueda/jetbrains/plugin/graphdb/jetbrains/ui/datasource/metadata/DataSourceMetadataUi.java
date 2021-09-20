package com.neueda.jetbrains.plugin.graphdb.jetbrains.ui.datasource.metadata;

import com.intellij.ui.treeStructure.PatchedDefaultMutableTreeNode;
import com.neueda.jetbrains.plugin.graphdb.database.api.data.App;
import com.neueda.jetbrains.plugin.graphdb.database.api.data.GraphEntity;
import com.neueda.jetbrains.plugin.graphdb.database.api.data.GraphField;
import com.neueda.jetbrains.plugin.graphdb.database.api.data.Workspace;
import com.neueda.jetbrains.plugin.graphdb.jetbrains.component.datasource.DataSourceType;
import com.neueda.jetbrains.plugin.graphdb.jetbrains.component.datasource.metadata.DataSourcesComponentMetadata;
import com.neueda.jetbrains.plugin.graphdb.jetbrains.component.datasource.metadata.Neo4jBoltCypherDataSourceMetadata;
import com.neueda.jetbrains.plugin.graphdb.jetbrains.component.datasource.state.DataSourceApi;
import com.neueda.jetbrains.plugin.graphdb.jetbrains.ui.datasource.tree.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;

import static com.neueda.jetbrains.plugin.graphdb.jetbrains.component.datasource.DataSourceType.OPENCYPHER_GREMLIN;
import static com.neueda.jetbrains.plugin.graphdb.jetbrains.ui.datasource.tree.Neo4jTreeNodeType.*;
import static java.util.concurrent.CompletableFuture.completedFuture;

public class DataSourceMetadataUi {

    private final DataSourcesComponentMetadata dataSourcesComponent;

    private final Map<DataSourceType, BiFunction<PatchedDefaultMutableTreeNode, Neo4jBoltCypherDataSourceMetadata, Boolean>> handlers = new HashMap<>();

    public DataSourceMetadataUi(DataSourcesComponentMetadata dataSourcesComponent) {
        this.dataSourcesComponent = dataSourcesComponent;

        handlers.put(OPENCYPHER_GREMLIN, this::updateOpenCypherGremlinMetadataUi);
    }

    public CompletableFuture<Boolean> updateDataSourceMetadataUi(PatchedDefaultMutableTreeNode node, DataSourceApi nodeDataSource) {
        DataSourceType sourceType = nodeDataSource.getDataSourceType();
        if (handlers.containsKey(sourceType)) {
            return dataSourcesComponent.getMetadata(nodeDataSource)
                    .thenApply((data) ->
                            data.map(genericMetadata -> (Neo4jBoltCypherDataSourceMetadata) genericMetadata)
                                    .map(neo4jMetadata -> handlers.get(sourceType).apply(node, neo4jMetadata))
                                    .orElse(false));
        } else {
            return completedFuture(false);
        }
    }

    boolean updateOpenCypherGremlinMetadataUi(PatchedDefaultMutableTreeNode dataSourceRootTreeNode,
                                              Neo4jBoltCypherDataSourceMetadata dataSourceMetadata) {
        // Remove existing metadata from ui
        dataSourceRootTreeNode.removeAllChildren();
        TreeNodeModelApi model = (TreeNodeModelApi) dataSourceRootTreeNode.getUserObject();
        DataSourceApi dataSourceApi = model.getDataSourceApi();

        List<Workspace> workspaces = dataSourceMetadata.getWorkspaces();
        for (Workspace workspaceDomain : workspaces) {
            dataSourceRootTreeNode.add(createWorkspaceNode(workspaceDomain, dataSourceApi));
        }

        return true;
    }

    private PatchedDefaultMutableTreeNode createWorkspaceNode(Workspace workspaceDomain, DataSourceApi dataSourceApi) {
        PatchedDefaultMutableTreeNode workspaceTreeNode = new PatchedDefaultMutableTreeNode(
                new WorkspaceTypeTreeNodeModel(WORKSPACE, dataSourceApi, workspaceDomain,
                        workspaceDomain.getApps().size()));

        for (App appDomain : workspaceDomain.getApps()) {

            AppTypeTreeNodeModel appTypeTreeNodeModel = new AppTypeTreeNodeModel(APP, dataSourceApi,
                    appDomain, appDomain.getEntities().size());
            PatchedDefaultMutableTreeNode appTypeTreeNode = of(appTypeTreeNodeModel);

            List<GraphEntity> entities = appDomain.getEntities();

            for (GraphEntity entityDomain : entities) {
                EntityTypeTreeNodeModel entityTypeTreeNodeModel = new EntityTypeTreeNodeModel(ENTITY, dataSourceApi,
                        entityDomain, entityDomain.getFields().size());

                PatchedDefaultMutableTreeNode entityTypeTreeNode = of(entityTypeTreeNodeModel);

                List<GraphField> fields = entityDomain.getFields();

                for (GraphField fieldDomain : fields) {
                    FieldTypeTreeNodeModel fieldTypeTreeNodeModel = new FieldTypeTreeNodeModel(PROPERTY_KEY,
                            dataSourceApi, fieldDomain);
                    entityTypeTreeNode.add(of(fieldTypeTreeNodeModel));
                }

                appTypeTreeNode.add(entityTypeTreeNode);
            }

            workspaceTreeNode.add(appTypeTreeNode);
        }

        return workspaceTreeNode;
    }

    private PatchedDefaultMutableTreeNode of(MetadataTreeNodeModel model) {
        return new PatchedDefaultMutableTreeNode(model);
    }
}
