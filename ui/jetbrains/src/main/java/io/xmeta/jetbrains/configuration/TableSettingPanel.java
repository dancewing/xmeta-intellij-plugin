package io.xmeta.jetbrains.configuration;

import com.fasterxml.jackson.core.type.TypeReference;
import io.xmeta.generator.config.ConfigService;
import io.xmeta.generator.model.ColumnConfig;
import io.xmeta.generator.model.ColumnConfigGroup;
import io.xmeta.generator.model.ColumnConfigType;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.project.Project;
import io.xmeta.jetbrains.util.CloneUtils;
import io.xmeta.jetbrains.util.ProjectUtils;
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
    /**
     * 项目对象
     */
    private Project project;

    public TableSettingPanel() {

        super(CloneUtils.cloneByJson(ConfigService.getInstance(ProjectUtils.getCurrProject()).getColumnConfigGroupMap(),
                new TypeReference<Map<String, ColumnConfigGroup>>() {}), ConfigService.getInstance(ProjectUtils.getCurrProject()).getCurrColumnConfigGroupName());
        this.project = ProjectUtils.getCurrProject();
        this.settings = ConfigService.getInstance(this.project);
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
        // 配置服务实例化, 重新加载，因为数据发生变更
        this.settings = ConfigService.getInstance(this.project);
        this.group = CloneUtils.cloneByJson(settings.getColumnConfigGroupMap(), new TypeReference<Map<String, ColumnConfigGroup>>() {});
        this.currGroupName = settings.getCurrColumnConfigGroupName();
        init();
    }
}
