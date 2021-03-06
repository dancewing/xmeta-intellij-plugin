package io.xmeta.jetbrains.ui.datasource.interactions;

import com.intellij.openapi.project.Project;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.treeStructure.PatchedDefaultMutableTreeNode;
import com.intellij.ui.treeStructure.Tree;
import io.xmeta.jetbrains.component.datasource.state.DataSource;
import io.xmeta.jetbrains.ui.datasource.DataSourcesView;
import io.xmeta.jetbrains.ui.datasource.tree.Neo4jTreeNodeType;
import io.xmeta.jetbrains.ui.datasource.tree.TreeNodeModelApi;
import io.xmeta.jetbrains.util.FileUtil;
import io.xmeta.jetbrains.util.Notifier;

import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DataSourceInteractions {

    private final DataSourcesView dataSourcesView;
    private final ToolbarDecorator decorator;
    private final Project project;
    private final Tree dataSourceTree;

    public DataSourceInteractions(Project project, DataSourcesView dataSourcesView) {
        this.project = project;
        this.dataSourcesView = dataSourcesView;
        this.decorator = dataSourcesView.getDecorator();
        this.dataSourceTree = dataSourcesView.getDataSourceTree();

        initAddAction();
        initRemoveAction();
        initEditAction();
        initQuickEditorAction();
    }

    private void initAddAction() {
        decorator.setAddAction(anActionButton -> {
            OpenCypherGremlinDataSourceDialog dialog = new OpenCypherGremlinDataSourceDialog(project, dataSourcesView);
            if (dialog.go()) {
                dataSourcesView.createDataSource(dialog.constructDataSource());
            }
        });
    }

    private void initRemoveAction() {
        decorator.setRemoveAction(anActionButton -> {
            DefaultMutableTreeNode[] selectedNodes = dataSourceTree.getSelectedNodes(DefaultMutableTreeNode.class,
                    this::isDataSource);

            List<DataSource> dataSourcesForRemoval = Arrays.stream(selectedNodes)
                    .map(this::getDataSourceApi)
                    .collect(Collectors.toList());

            if (dataSourcesForRemoval.size() > 0) {
                dataSourcesView.removeDataSources(project, dataSourcesForRemoval);
            }
        });
        decorator.setRemoveActionUpdater(e -> {
            DefaultMutableTreeNode[] selectedNodes = dataSourceTree.getSelectedNodes(DefaultMutableTreeNode.class,
                    this::isDataSource);

            return selectedNodes.length > 0;
        });
    }

    private void initEditAction() {
        decorator.setEditActionUpdater(e -> {
            DefaultMutableTreeNode[] selectedNodes = dataSourceTree.getSelectedNodes(DefaultMutableTreeNode.class,
                    this::isDataSource);

            return selectedNodes.length == 1;
        });
        decorator.setEditAction(anActionButton -> {
            PatchedDefaultMutableTreeNode[] selectedNodes = dataSourceTree.getSelectedNodes(PatchedDefaultMutableTreeNode.class,
                    (node) -> node.getUserObject() instanceof TreeNodeModelApi);

            if (selectedNodes.length == 1) {
                PatchedDefaultMutableTreeNode treeNode = selectedNodes[0];

                DataSource dataSourceToEdit = getDataSourceApi(treeNode);

                DataSourceDialog dialog = new OpenCypherGremlinDataSourceDialog(project, dataSourcesView, dataSourceToEdit);
                ;
                if (dialog.go()) {
                    dataSourcesView.updateDataSource(treeNode, dataSourceToEdit, dialog.constructDataSource());
                }
            }
        });
    }

    private void initQuickEditorAction() {
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int clickCount = e.getClickCount();
                if (clickCount == 2) {
                    DefaultMutableTreeNode[] selectedNodes = dataSourceTree.getSelectedNodes(DefaultMutableTreeNode.class,
                            DataSourceInteractions.this::isDataSource);

                    if (selectedNodes.length != 1) {
                        return;
                    }

                    DataSource dataSource = getDataSourceApi(selectedNodes[0]);

                    try {
                        FileUtil.openFile(project, FileUtil.getDataSourceFile(project, dataSource));
                    } catch (IOException exception) {
                        Notifier.error("Open editor error", exception.getMessage());
                    }
                }
            }
        };
        dataSourceTree.addMouseListener(mouseAdapter);
    }

    private boolean isDataSource(DefaultMutableTreeNode node) {
        return node.getUserObject() instanceof TreeNodeModelApi
                && ((TreeNodeModelApi) node.getUserObject()).getType() == Neo4jTreeNodeType.DATASOURCE;
    }

    private DataSource getDataSourceApi(DefaultMutableTreeNode node) {
        return ((TreeNodeModelApi) node.getUserObject()).getDataSourceApi();
    }
}
