package io.xmeta.jetbrains.ui.datasource;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.ActionToolbarPosition;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.ui.treeStructure.PatchedDefaultMutableTreeNode;
import com.intellij.ui.treeStructure.Tree;
import io.xmeta.jetbrains.component.datasource.metadata.DataSourcesComponentMetadata;
import io.xmeta.jetbrains.component.datasource.state.DataSource;
import io.xmeta.jetbrains.configuration.DataSourcesSettings;
import io.xmeta.jetbrains.ui.datasource.actions.RefreshDataSourcesAction;
import io.xmeta.jetbrains.ui.datasource.interactions.DataSourceInteractions;
import io.xmeta.jetbrains.ui.datasource.metadata.DataSourceMetadataUi;
import io.xmeta.jetbrains.ui.datasource.tree.*;
import io.xmeta.jetbrains.util.FileUtil;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class DataSourcesView implements Disposable {

    private boolean initialized;

    private DataSourcesSettings component;
    private DataSourcesComponentMetadata componentMetadata;
    private DataSourceInteractions interactions;
    private PatchedDefaultMutableTreeNode treeRoot;
    private DefaultTreeModel treeModel;

    private JPanel toolWindowContent;
    private JPanel treePanel;
    private Tree dataSourceTree;
    private ToolbarDecorator decorator;
    private DataSourceMetadataUi dataSourceMetadataUi;

    public DataSourcesView() {
        initialized = false;
    }

    public void initToolWindow(Project project, ToolWindow toolWindow) {
        if (!initialized) {
            ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
            Content content = contentFactory.createContent(toolWindowContent, "", false);
            toolWindow.getContentManager().addContent(content);

            component = DataSourcesSettings.getInstance(project);
            componentMetadata = project.getComponent(DataSourcesComponentMetadata.class);
            dataSourceMetadataUi = new DataSourceMetadataUi(componentMetadata);
            treeRoot = new PatchedDefaultMutableTreeNode(new RootTreeNodeModel());
            treeModel = new DefaultTreeModel(treeRoot, false);
            decorator = ToolbarDecorator.createDecorator(dataSourceTree);
            decorator.addExtraAction(new RefreshDataSourcesAction(this));

            configureDataSourceTree();
            decorateDataSourceTree();

            interactions = new DataSourceInteractions(project, this);

            replaceTreeWithDecorated();
            showDataSources();
            refreshDataSourcesMetadata();

            initialized = true;
        }
    }

    public DataSourcesSettings getComponent() {
        return component;
    }

    public Tree getDataSourceTree() {
        return dataSourceTree;
    }

    public PatchedDefaultMutableTreeNode getTreeRoot() {
        return treeRoot;
    }

    public DefaultTreeModel getTreeModel() {
        return treeModel;
    }

    public ToolbarDecorator getDecorator() {
        return decorator;
    }

    private void createUIComponents() {
        treePanel = new JPanel(new GridLayout(0, 1));
    }

    private void configureDataSourceTree() {
        dataSourceTree.getEmptyText().setText("Create a data source");
        dataSourceTree.setCellRenderer(new GraphColoredTreeCellRenderer());
        dataSourceTree.setModel(treeModel);
        dataSourceTree.setRootVisible(false);
        dataSourceTree.setToggleClickCount(0);
        dataSourceTree.addMouseListener(new TreeMouseAdapter());
    }

    private void decorateDataSourceTree() {
        decorator.setPanelBorder(BorderFactory.createEmptyBorder());
        decorator.setToolbarPosition(ActionToolbarPosition.TOP);
    }

    private void replaceTreeWithDecorated() {
        JPanel panel = decorator.createPanel();
        treePanel.add(panel);
    }

    private void showDataSources() {
        component.getDataSources()
                .forEach((dataSource) -> treeRoot.add(new PatchedDefaultMutableTreeNode(new DataSourceTreeNodeModel(dataSource))));
        treeModel.reload();
    }

    public void refreshDataSourcesMetadata() {
        Enumeration children = treeRoot.children();
        while (children.hasMoreElements()) {
            refreshDataSourceMetadata((PatchedDefaultMutableTreeNode) children.nextElement())
                    .thenAccept((isRefreshed) -> {
                        if (isRefreshed) {
                            treeModel.reload();
                        }
                    });
        }
    }

    public CompletableFuture<Boolean> refreshDataSourceMetadata(PatchedDefaultMutableTreeNode treeNode) {
        TreeNodeModelApi userObject = (TreeNodeModelApi) treeNode.getUserObject();
        DataSource nodeDataSource = userObject.getDataSourceApi();
        return dataSourceMetadataUi.updateDataSourceMetadataUi(treeNode, nodeDataSource);
    }

    public void createDataSource(DataSource dataSource) {
        component.addDataSource(dataSource);
        TreeNodeModelApi model = new DataSourceTreeNodeModel(dataSource);
        PatchedDefaultMutableTreeNode treeNode = new PatchedDefaultMutableTreeNode(model);
        treeRoot.add(treeNode);
        refreshDataSourceMetadata(treeNode);
        treeModel.reload();
    }

    public void updateDataSource(PatchedDefaultMutableTreeNode treeNode, DataSource oldDataSource, DataSource newDataSource) {
        component.updateDataSource(oldDataSource, newDataSource);
        treeNode.setUserObject(new DataSourceTreeNodeModel(newDataSource));
        refreshDataSourceMetadata(treeNode);
        treeModel.reload();
    }

    public void removeDataSources(Project project, List<DataSource> dataSourcesForRemoval) {
        component.removeDataSources(dataSourcesForRemoval);

        dataSourcesForRemoval.stream()
                .peek((dataSourceApi -> {
                    ApplicationManager.getApplication().runWriteAction(() -> {
                        try {
                            FileUtil.getDataSourceFile(project, dataSourceApi).delete(project);
                        } catch (IOException e) {
                            // do nothing
                        }
                    });
                }))
                .map(DataSource::getName)
                .map(name -> {
                    Enumeration enumeration = treeRoot.children();
                    while (enumeration.hasMoreElements()) {
                        DefaultMutableTreeNode element = (DefaultMutableTreeNode) enumeration.nextElement();
                        TreeNodeModelApi userObject = (TreeNodeModelApi) element.getUserObject();
                        DataSource dataSource = userObject.getDataSourceApi();
                        if (dataSource.getName().equals(name)) {
                            return element;
                        }
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .forEach(treeRoot::remove);

        treeModel.reload();
    }

    @Override
    public void dispose() {
    }
}
