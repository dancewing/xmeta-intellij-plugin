package io.xmeta.jetbrains.ui.datasource.metadata;

import com.intellij.ui.treeStructure.PatchedDefaultMutableTreeNode;
import io.xmeta.api.data.App;
import io.xmeta.api.data.GraphEntity;
import io.xmeta.api.data.GraphField;
import io.xmeta.api.data.Workspace;
import io.xmeta.jetbrains.component.datasource.DataSourceType;
import io.xmeta.jetbrains.component.datasource.metadata.DataSourcesComponentMetadata;
import io.xmeta.jetbrains.component.datasource.metadata.Neo4jBoltCypherDataSourceMetadata;
import io.xmeta.jetbrains.component.datasource.state.DataSource;
import io.xmeta.jetbrains.ui.datasource.tree.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;

import static io.xmeta.jetbrains.component.datasource.DataSourceType.OPENCYPHER_GREMLIN;
import static java.util.concurrent.CompletableFuture.completedFuture;

public class DataSourceMetadataUi {

    private final DataSourcesComponentMetadata dataSourcesComponent;

    private final Map<DataSourceType, BiFunction<PatchedDefaultMutableTreeNode, Neo4jBoltCypherDataSourceMetadata, Boolean>> handlers = new HashMap<>();

    public DataSourceMetadataUi(DataSourcesComponentMetadata dataSourcesComponent) {
        this.dataSourcesComponent = dataSourcesComponent;

        handlers.put(OPENCYPHER_GREMLIN, this::updateOpenCypherGremlinMetadataUi);
    }

    public CompletableFuture<Boolean> updateDataSourceMetadataUi(PatchedDefaultMutableTreeNode node, DataSource nodeDataSource) {
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
        DataSource dataSourceApi = model.getDataSourceApi();

        List<Workspace> workspaces = dataSourceMetadata.getWorkspaces();
        for (Workspace workspaceDomain : workspaces) {
            dataSourceRootTreeNode.add(createWorkspaceNode(workspaceDomain, dataSourceApi));
        }

        return true;
    }

    private PatchedDefaultMutableTreeNode createWorkspaceNode(Workspace workspaceDomain, DataSource dataSourceApi) {
        PatchedDefaultMutableTreeNode workspaceTreeNode = new PatchedDefaultMutableTreeNode(
                new WorkspaceTypeTreeNodeModel(Neo4jTreeNodeType.WORKSPACE, dataSourceApi, workspaceDomain,
                        workspaceDomain.getApps().size()));

        for (App appDomain : workspaceDomain.getApps()) {

            AppTypeTreeNodeModel appTypeTreeNodeModel = new AppTypeTreeNodeModel(Neo4jTreeNodeType.APP, dataSourceApi,
                    appDomain, appDomain.getEntities().size());
            PatchedDefaultMutableTreeNode appTypeTreeNode = of(appTypeTreeNodeModel);

            List<GraphEntity> entities = appDomain.getEntities();

            for (GraphEntity entityDomain : entities) {
                EntityTypeTreeNodeModel entityTypeTreeNodeModel = new EntityTypeTreeNodeModel(Neo4jTreeNodeType.ENTITY, dataSourceApi,
                        entityDomain, entityDomain.getFields().size());

                PatchedDefaultMutableTreeNode entityTypeTreeNode = of(entityTypeTreeNodeModel);

                List<GraphField> fields = entityDomain.getFields();

                for (GraphField fieldDomain : fields) {
                    FieldTypeTreeNodeModel fieldTypeTreeNodeModel = new FieldTypeTreeNodeModel(Neo4jTreeNodeType.PROPERTY_KEY,
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
