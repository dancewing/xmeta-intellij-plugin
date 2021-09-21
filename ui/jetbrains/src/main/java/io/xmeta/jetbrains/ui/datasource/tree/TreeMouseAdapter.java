package io.xmeta.jetbrains.ui.datasource.tree;

import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.ui.components.labels.LinkLabel;
import com.intellij.ui.treeStructure.PatchedDefaultMutableTreeNode;
import com.intellij.ui.treeStructure.Tree;
import io.xmeta.jetbrains.ui.helpers.UiHelper;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TreeMouseAdapter extends MouseAdapter {

    private ContextMenuService contextMenuService = new ContextMenuService();

    @Override
    public void mouseClicked(MouseEvent e) {
        Tree tree = (Tree) e.getComponent();
        TreePath pathForLocation = tree.getPathForLocation(e.getX(), e.getY());

        if (SwingUtilities.isLeftMouseButton(e)) {
            Optional.ofNullable(pathForLocation)
                    .flatMap(p -> UiHelper.cast(p.getLastPathComponent(), PatchedDefaultMutableTreeNode.class))
                    .flatMap(n -> UiHelper.cast(n.getUserObject(), LinkLabel.class))
                    .ifPresent(LinkLabel::doClick);
        } else if (SwingUtilities.isRightMouseButton(e)) {
            DataContext dataContext = DataManager.getInstance().getDataContext(tree);
            contextMenuService.getContextMenu(pathForLocation)
                    .ifPresent(dto -> dto.showPopup(dataContext, getSelectedData(tree)));
        }
    }

    private List<TreeNodeModelApi> getSelectedData(Tree tree) {
        TreePath[] selectionPaths = tree.getSelectionPaths();
        List<TreeNodeModelApi> selectedData = new ArrayList<>();
        for (TreePath path : selectionPaths) {
            if (path.getLastPathComponent() instanceof PatchedDefaultMutableTreeNode) {
                PatchedDefaultMutableTreeNode lastPathComponent = (PatchedDefaultMutableTreeNode) path.getLastPathComponent();
                if (lastPathComponent.getUserObject() instanceof TreeNodeModelApi) {
                    selectedData.add((TreeNodeModelApi) lastPathComponent.getUserObject());
                }
            }
        }
        return selectedData;
    }
}
