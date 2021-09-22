package io.xmeta.jetbrains.configuration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.intellij.openapi.options.Configurable;
import io.xmeta.generator.config.ConfigService;
import io.xmeta.generator.model.ColumnConfig;
import io.xmeta.generator.model.ColumnConfigGroup;
import io.xmeta.generator.model.ColumnConfigType;
import io.xmeta.jetbrains.util.CloneUtils;
import io.xmeta.jetbrains.util.ProjectUtils;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 表设置面板
 *
 * @author makejava
 * @version 1.0.0
 * @since 2018/07/17 13:10
 */
public class MappingSettingPanel extends AbstractTableGroupPanel<ColumnConfigGroup, ColumnConfig> implements Configurable {

    public MappingSettingPanel() {

        super(CloneUtils.cloneByJson(ConfigService.getInstance(ProjectUtils.getCurrProject()).getColumnConfigGroupMap(),
                new TypeReference<Map<String, ColumnConfigGroup>>() {}), ConfigService.getInstance(ProjectUtils.getCurrProject()).getCurrColumnConfigGroupName());
    }

    @Override
    protected Object[] toRow(ColumnConfig item) {
         return new Object[]{item.getType().name(), item.getLongName(), item.getUiType().name(),
                 item.getSupportTypes()==null? new String[]{}: item.getSupportTypes().stream().map(Enum::name).collect(Collectors.toList()) };
    }

    @Override
    protected ColumnConfig toItem(Object[] rowData) {
        // return new ColumnConfig((String) rowData[0], ColumnConfigType.valueOf((String) rowData[1]),
      //          (String) rowData[2]);
        return null;
    }

    @Override
    protected String getItemName(ColumnConfig item) {
        return item.getType().name();
    }

    @Override
    protected ColumnConfig createItem(String value) {
       // return new ColumnConfig(value, ColumnConfigType.SingleLineText);
        return null;
    }

    @Override
    protected String[] initColumn() {
        return new String[]{"Field Type", "JavaType", "UIType", "Support UI Types"};
    }

    @Override
    public String getDisplayName() {
        return "Mapping Setting";
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
       // settings.setColumnConfigGroupMap(group);
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
