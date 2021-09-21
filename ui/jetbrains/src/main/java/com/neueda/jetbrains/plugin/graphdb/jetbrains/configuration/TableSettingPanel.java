package com.neueda.jetbrains.plugin.graphdb.jetbrains.configuration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.houkunlin.config.ConfigService;
import com.github.houkunlin.model.ColumnConfig;
import com.github.houkunlin.model.ColumnConfigGroup;
import com.github.houkunlin.model.ColumnConfigType;
import com.intellij.openapi.options.Configurable;
import com.neueda.jetbrains.plugin.graphdb.jetbrains.util.CloneUtils;
import com.neueda.jetbrains.plugin.graphdb.jetbrains.util.ProjectUtils;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Map;

/**
 * 表设置面板
 *
 * @author makejava
 * @version 1.0.0
 * @since 2018/07/17 13:10
 */
public class TableSettingPanel extends AbstractTableGroupPanel<ColumnConfigGroup, ColumnConfig> implements Configurable {

    private ConfigService settings;

    public TableSettingPanel() {

        super(CloneUtils.cloneByJson(ConfigService.getInstance(ProjectUtils.getCurrProject()).getColumnConfigGroupMap(),
                new TypeReference<Map<String, ColumnConfigGroup>>() {}), ConfigService.getInstance(ProjectUtils.getCurrProject()).getCurrColumnConfigGroupName());
        this.settings = ConfigService.getInstance(ProjectUtils.getCurrProject());

    }

    @Override
    protected Object[] toRow(ColumnConfig item) {
        return new Object[]{item.getTitle(), item.getType().name(), item.getSelectValue()};
    }

    @Override
    protected ColumnConfig toItem(Object[] rowData) {
        return new ColumnConfig((String) rowData[0], ColumnConfigType.valueOf((String) rowData[1]), (String) rowData[2]);
    }

    @Override
    protected String getItemName(ColumnConfig item) {
        return item.getTitle();
    }

    @Override
    protected ColumnConfig createItem(String value) {
        return new ColumnConfig(value, ColumnConfigType.TEXT);
    }

    @Override
    protected ColumnConfig[] initColumn() {
        ColumnConfig[] columnConfigs = new ColumnConfig[3];
        columnConfigs[0] = new ColumnConfig("title", ColumnConfigType.TEXT);
        columnConfigs[1] = new ColumnConfig("type", ColumnConfigType.SELECT, "TEXT,SELECT,BOOLEAN");
        columnConfigs[2] = new ColumnConfig("selectValue", ColumnConfigType.TEXT);
        return columnConfigs;
    }

    @Override
    public String getDisplayName() {
        return "Table Editor Config";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        return getMainPanel();
    }

    @Override
    public boolean isModified() {
        refresh();
        return !settings.getColumnConfigGroupMap().equals(group) || !settings.getCurrColumnConfigGroupName().equals(currGroupName);
    }

    @Override
    public void apply() {
        settings.setColumnConfigGroupMap(group);
        settings.setCurrColumnConfigGroupName(currGroupName);
    }

    @Override
    public void reset() {
        this.group = CloneUtils.cloneByJson(settings.getColumnConfigGroupMap(), new TypeReference<Map<String, ColumnConfigGroup>>() {});
        this.currGroupName = settings.getCurrColumnConfigGroupName();
        init();
    }
}
