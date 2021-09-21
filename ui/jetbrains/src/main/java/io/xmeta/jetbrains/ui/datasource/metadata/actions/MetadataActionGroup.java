package io.xmeta.jetbrains.ui.datasource.metadata.actions;

import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import io.xmeta.api.data.MetaEntity;
import io.xmeta.api.data.IDNameData;
import io.xmeta.jetbrains.ui.datasource.tree.EntityTypeTreeNodeModel;
import io.xmeta.jetbrains.ui.datasource.tree.Neo4jTreeNodeType;
import io.xmeta.jetbrains.ui.datasource.tree.NodeType;
import io.xmeta.jetbrains.ui.datasource.tree.TreeNodeModelApi;
import icons.MetaIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static icons.MetaIcons.Database.NEO4J;

public class MetadataActionGroup extends ActionGroup {

    private final NodeType type;
    private final IDNameData data;
    private final String dataSourceUuid;
    private final List<TreeNodeModelApi> selectedData;

    public MetadataActionGroup(NodeType type, IDNameData data, String dataSourceUuid, List<TreeNodeModelApi> selectedData) {
        this.type = type;
        this.data = data;
        this.dataSourceUuid = dataSourceUuid;
        this.selectedData = selectedData;
    }

    @NotNull
    @Override
    public AnAction[] getChildren(@Nullable AnActionEvent e) {
        if (type == Neo4jTreeNodeType.APP) {
            return new AnAction[]{new MetadataRelationshipAction(data, dataSourceUuid, "Query this relationship", "", NEO4J)};
        } else if (type == Neo4jTreeNodeType.ENTITY) {
            return new AnAction[]{
                    new ServerCodeGeneratorAction(getEntities(), "Generate Server Code", "", MetaIcons.Server),
                    new ClientCodeGeneratorAction(getEntities(), "Generate Client Code", "", MetaIcons.Client),
            };
        } else if (type == Neo4jTreeNodeType.PROPERTY_KEY) {
            return new AnAction[]{new MetadataPropertyKeyAction(data, dataSourceUuid, "Query this property", "", NEO4J)};
        } else {
            return new AnAction[]{};
        }
    }

    private List<MetaEntity> getEntities() {
        List<MetaEntity> graphEntities = new ArrayList<>();
        for (TreeNodeModelApi treeNodeModel : selectedData) {
            if (treeNodeModel instanceof EntityTypeTreeNodeModel) {
                EntityTypeTreeNodeModel entityTypeTreeNodeModel = (EntityTypeTreeNodeModel) treeNodeModel;
                entityTypeTreeNodeModel.getValue().ifPresent(graphEntities::add);
            }
        }
        return graphEntities;
    }
}
