package com.neueda.jetbrains.plugin.graphdb.jetbrains.configuration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.houkunlin.config.ConfigService;
import com.github.houkunlin.model.AbstractGroup;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.UnnamedConfigurable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogBuilder;
import com.intellij.openapi.ui.MessageDialogBuilder;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.ex.MultiLineLabel;
import com.intellij.util.ExceptionUtil;
import com.neueda.jetbrains.plugin.graphdb.jetbrains.configuration.base.ListCheckboxPanel;
import com.neueda.jetbrains.plugin.graphdb.jetbrains.util.MsgValue;
import com.neueda.jetbrains.plugin.graphdb.jetbrains.util.ProjectUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 主设置面板
 *
 * @author makejava
 * @version 1.0.0
 * @since 2018/07/17 13:10
 */
public class MainSetting implements Configurable, Configurable.Composite {
    /**
     * 主面板
     */
    private JPanel mainPanel;
    /**
     * 编码选择下拉框
     */
    private JComboBox encodeComboBox;
    /**
     * 作者编辑框
     */
    private JTextField authorTextField;
    /**
     * 重置默认设置按钮
     */
    private JButton resetBtn;
    /**
     * 模板导出按钮
     */
    private JButton exportBtn;
    /**
     * 当前版本号
     */
    private JTextField emailTextField;

    /**
     * 重置列表
     */
    private List<Configurable> resetList;

    /**
     * 需要保存的列表
     */
    private List<Configurable> saveList;

    /**
     * 所有列表
     */
    private List<Configurable> allList;

    /**
     * 设置对象
     */
    private ConfigService settings;

    /**
     * 默认构造方法
     */
    public MainSetting() {
        // 获取当前项目
        Project project = ProjectUtils.getCurrProject();
        settings = ConfigService.getInstance(project);
        init();

        //初始化事件

        //重置配置信息
        resetBtn.addActionListener(e -> {
            if (MessageDialogBuilder.yesNo(MsgValue.TITLE_INFO, MsgValue.RESET_DEFAULT_SETTING_MSG).isYes()) {
                if (CollectionUtils.isEmpty(resetList)) {
                    return;
                }
                // 初始化默认配置
               // settings.initDefault();
                // 重置
                resetList.forEach(UnnamedConfigurable::reset);
                if (CollectionUtils.isEmpty(saveList)) {
                    return;
                }
                // 保存
                saveList.forEach(configurable -> {
                    try {
                        configurable.apply();
                    } catch (ConfigurationException e1) {
                        e1.printStackTrace();
                    }
                });
            }
        });

        // 模板导出事件
        exportBtn.addActionListener(e -> {
            // 创建一行四列的主面板
            JPanel mainPanel = new JPanel(new GridLayout(1, 4));
            // Type Mapper
            ListCheckboxPanel typeMapperPanel = new ListCheckboxPanel("Type Mapper", settings.getTypeMapperGroupMap().keySet());
            mainPanel.add(typeMapperPanel);
            // Template
            ListCheckboxPanel templatePanel = new ListCheckboxPanel("Template", settings.getTemplateGroupMap().keySet());
            mainPanel.add(templatePanel);
            // Column Config
            ListCheckboxPanel columnConfigPanel = new ListCheckboxPanel("Column Config", settings.getColumnConfigGroupMap().keySet());
            mainPanel.add(columnConfigPanel);
            // GlobalConfig
            ListCheckboxPanel globalConfigPanel = new ListCheckboxPanel("Global Config", settings.getGlobalConfigGroupMap().keySet());
            mainPanel.add(globalConfigPanel);
            // 构建dialog
            DialogBuilder dialogBuilder = new DialogBuilder(project);
            dialogBuilder.setTitle(MsgValue.TITLE_INFO);
            dialogBuilder.setNorthPanel(new MultiLineLabel("请选择要导出的配置分组："));
            dialogBuilder.setCenterPanel(mainPanel);
            dialogBuilder.addActionDescriptor(dialogWrapper -> new AbstractAction("OK") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!MainSetting.this.isSelected(typeMapperPanel, templatePanel, columnConfigPanel, globalConfigPanel)) {
                        Messages.showWarningDialog("至少选择一个模板组！", MsgValue.TITLE_INFO);
                        return;
                    }
                }
            });
            dialogBuilder.show();
        });
    }

    /**
     * 判断是否选中
     *
     * @param checkboxPanels 复选框面板
     * @return 是否选中
     */
    private boolean isSelected(@NotNull ListCheckboxPanel... checkboxPanels) {
        for (ListCheckboxPanel checkboxPanel : checkboxPanels) {
            if (!CollectionUtils.isEmpty(checkboxPanel.getSelectedItems())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 覆盖配置
     *
     * @param jsonNode json节点对象
     * @param name     配置组名称
     * @param cls      配置组类
     * @param srcGroup 源分组
     */
    private <T extends AbstractGroup> void coverConfig(@NotNull JsonNode jsonNode, @NotNull String name, Class<T> cls, @NotNull Map<String, T> srcGroup) {
        ObjectMapper objectMapper = new ObjectMapper();
        if (!jsonNode.has(name)) {
            return;
        }
        try {
            JsonNode node = jsonNode.get(name);
            if (node.size() == 0) {
                return;
            }
            // 覆盖配置
            Iterator<String> names = node.fieldNames();
            while (names.hasNext()) {
                String key = names.next();
                String value = node.get(key).toString();
                T group = objectMapper.readValue(value, cls);
                if (srcGroup.containsKey(key)) {
                    if (!MessageDialogBuilder.yesNo(MsgValue.TITLE_INFO, String.format("是否覆盖%s配置中的%s分组？", name, key)).isYes()) {
                        continue;
                    }
                }
                srcGroup.put(key, group);
            }
        } catch (IOException e) {
            Messages.showWarningDialog("JSON解析错误！", MsgValue.TITLE_INFO);
            ExceptionUtil.rethrow(e);
        }
    }

    /**
     * 初始化方法
     */
    private void init() {
        //初始化数据
        authorTextField.setText(settings.getBaseSettings().getAuthor());
        encodeComboBox.setSelectedItem(settings.getBaseSettings().getEncode());
        emailTextField.setText(settings.getBaseSettings().getEmail());
    }

    /**
     * 设置显示名称
     *
     * @return 显示名称
     */
    @Override
    public String getDisplayName() {
        return "XMeta";
    }

    /**
     * 更多配置
     *
     * @return 配置选项
     */
    @NotNull
    @Override
    public Configurable[] getConfigurables() {
        Configurable[] result = new Configurable[4];
        result[0] = new TypeMapperSetting(settings);
        result[1] = new TemplateSettingPanel();
        result[2] = new TableSettingPanel();
        result[3] = new GlobalConfigSettingPanel();
        // 所有列表
        allList = new ArrayList<>();
        allList.add(result[0]);
        allList.add(result[1]);
        allList.add(result[2]);
        allList.add(result[3]);
        // 需要重置的列表
        resetList = new ArrayList<>();
        resetList.add(result[0]);
        resetList.add(result[1]);
        resetList.add(result[3]);
        // 不需要重置的列表
        saveList = new ArrayList<>();
        saveList.add(this);
        saveList.add(result[2]);
        return result;
    }

    /**
     * 获取主面板信息
     *
     * @return 主面板
     */
    @Nullable
    @Override
    public JComponent createComponent() {
        return mainPanel;
    }

    /**
     * 判断是否修改
     *
     * @return 是否修改
     */
    @Override
    public boolean isModified() {
        return !StringUtils.equals(settings.getBaseSettings().getEncode(), (String)encodeComboBox.getSelectedItem()) ||
                !StringUtils.equals(settings.getBaseSettings().getAuthor(), authorTextField.getText());
    }

    /**
     * 应用修改
     */
    @Override
    public void apply() {
        //保存数据
        settings.getBaseSettings().setAuthor(authorTextField.getText());
        settings.getBaseSettings().setEmail(emailTextField.getText());
        settings.getBaseSettings().setEncode((String) encodeComboBox.getSelectedItem());
    }

    /**
     * 重置
     */
    @Override
    public void reset() {
        init();
    }
}
