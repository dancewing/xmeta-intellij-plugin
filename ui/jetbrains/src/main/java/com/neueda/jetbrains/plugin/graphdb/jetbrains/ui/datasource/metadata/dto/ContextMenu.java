package com.neueda.jetbrains.plugin.graphdb.jetbrains.ui.datasource.metadata.dto;

import com.intellij.openapi.actionSystem.DataContext;
import com.neueda.jetbrains.plugin.graphdb.jetbrains.ui.datasource.tree.TreeNodeModelApi;

import java.util.List;

public interface ContextMenu {
    void showPopup(DataContext dataContext, List<TreeNodeModelApi> selectedData);
}
