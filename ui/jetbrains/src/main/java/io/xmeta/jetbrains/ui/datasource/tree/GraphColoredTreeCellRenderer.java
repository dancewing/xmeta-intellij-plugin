package io.xmeta.jetbrains.ui.datasource.tree;

import com.intellij.ui.ColoredTreeCellRenderer;
import com.intellij.ui.SimpleTextAttributes;
import io.xmeta.api.data.IDNameData;
import io.xmeta.jetbrains.component.datasource.state.DataSource;
import io.xmeta.jetbrains.ui.datasource.tree.dto.ValueWithIcon;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

public class GraphColoredTreeCellRenderer extends ColoredTreeCellRenderer {


    public GraphColoredTreeCellRenderer() {
    }

    @Override
    public void customizeCellRenderer(@NotNull JTree tree, Object value, boolean selected, boolean expanded,
                                      boolean leaf, int row, boolean hasFocus) {
        Object userObject = ((DefaultMutableTreeNode) value).getUserObject();

        if (userObject instanceof DataSource) {
            DataSource dataSource = (DataSource) userObject;
            setIcon(dataSource.getDescription().getIcon());
            append(dataSource.getName(), SimpleTextAttributes.REGULAR_BOLD_ATTRIBUTES, true);
        } else if (userObject instanceof ValueWithIcon) {
            ValueWithIcon val = (ValueWithIcon) userObject;
            setIcon(val.getIcon());
            append(val.getValue());
        } else if (userObject instanceof TreeNodeModelApi) {
            TreeNodeModelApi<IDNameData> model = (TreeNodeModelApi<IDNameData>) userObject;
            model.getIcon().ifPresent(this::setIcon);
            model.getText().ifPresent(this::append);
        } else if (userObject != null) {
            append(value.toString());
        }
    }
}
