package io.xmeta.jetbrains.configuration;

import com.fasterxml.jackson.core.type.TypeReference;
import io.xmeta.generator.config.ConfigService;
import io.xmeta.generator.model.TypeMapper;
import io.xmeta.generator.model.TypeMapperGroup;
import io.xmeta.generator.model.TypeMapperModel;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.InputValidator;
import com.intellij.openapi.ui.MessageDialogBuilder;
import com.intellij.openapi.ui.Messages;
import io.xmeta.jetbrains.util.CloneUtils;
import io.xmeta.jetbrains.util.MsgValue;
import io.xmeta.jetbrains.util.ProjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Map;

/**
 * 类型映射设置
 *
 * @author makejava
 * @version 1.0.0
 * @since 2018/07/17 13:10
 */
public class TypeMapperSetting implements Configurable {
    /**
     * 主面板
     */
    private JPanel mainPanel;
    /**
     * 类型映射分组切换下拉框
     */
    private JComboBox<String> typeMapperComboBox;
    /**
     * 分组复制按钮
     */
    private JButton typeMapperCopyButton;
    /**
     * 类型映射表
     */
    private JTable typeMapperTable;
    /**
     * 添加映射按钮
     */
    private JButton addButton;
    /**
     * 移除映射按钮
     */
    private JButton removeButton;
    /**
     * 删除分组按钮
     */
    private JButton deleteButton;

    /**
     * 是否初始化完成
     */
    private boolean init;
    /**
     * 类型映射表模型
     */
    private TypeMapperModel typeMapperModel;

    /**
     * 当前选中分组
     */
    private String currGroupName;
    /**
     * 类型映射分组集合
     */
    private Map<String, TypeMapperGroup> typeMapperGroupMap;
    /**
     * 全局配置服务
     */
    private ConfigService settings;

    /**
     * 项目对象
     */
    private Project project;

    public TypeMapperSetting() {
        this.project = ProjectUtils.getCurrProject();
        this.settings = ConfigService.getInstance(project);

        this.typeMapperGroupMap = CloneUtils.cloneByJson(settings.getTypeMapperGroupMap(), new TypeReference<Map<String, TypeMapperGroup>>() {});
        this.currGroupName = settings.getCurrTypeMapperGroupName();
        //添加类型
        addButton.addActionListener(e -> typeMapperModel.addRow(new TypeMapper("demoColumn", "java.lang.Object")));

        //移除类型
        removeButton.addActionListener(e -> {
            int[] selectRows = typeMapperTable.getSelectedRows();
            if (selectRows == null || selectRows.length == 0) {
                return;
            }
            if (!MessageDialogBuilder.yesNo(MsgValue.TITLE_INFO, "Confirm Delete Selected Item?").isYes()) {
                return;
            }
            // 从后面往前面移除，防止下标错位问题。
            for (int i = selectRows.length - 1; i >= 0; i--) {
                typeMapperModel.removeRow(selectRows[i]);
            }
        });

        //切换分组
        typeMapperComboBox.addActionListener(e -> {
            if (!init) {
                return;
            }
            String value = (String) typeMapperComboBox.getSelectedItem();
            if (value == null) {
                return;
            }
            if (currGroupName.equals(value)) {
                return;
            }
            currGroupName = value;
            refresh();
        });

        //复制分组按钮
        typeMapperCopyButton.addActionListener(e -> {
            String value = Messages.showInputDialog("Group Name:", "Input Group Name:", Messages.getQuestionIcon(), currGroupName + " Copy", new InputValidator() {
                @Override
                public boolean checkInput(String inputString) {
                    return !StringUtils.isEmpty(inputString) && !typeMapperGroupMap.containsKey(inputString);
                }

                @Override
                public boolean canClose(String inputString) {
                    return this.checkInput(inputString);
                }
            });
            if (value == null) {
                return;
            }
            // 克隆对象
            TypeMapperGroup typeMapperGroup = CloneUtils.cloneByJson(typeMapperGroupMap.get(currGroupName));
            typeMapperGroup.setName(value);
            typeMapperGroupMap.put(value, typeMapperGroup);
            currGroupName = value;
            refresh();
        });

        //删除分组
        deleteButton.addActionListener(e -> {
            if (MessageDialogBuilder.yesNo(MsgValue.TITLE_INFO, "Confirm Delete Group " + typeMapperComboBox.getSelectedItem() + "?").isYes()) {
                if (ConfigService.DEFAULT_NAME.equals(currGroupName)) {
                    Messages.showWarningDialog("Can't Delete Default Group!", MsgValue.TITLE_INFO);
                    return;
                }
                typeMapperGroupMap.remove(currGroupName);
                currGroupName = ConfigService.DEFAULT_NAME;
                refresh();
            }
        });

        // 初始化操作
        init();
    }


    /**
     * 初始化方法
     */
    private void init() {
        //初始化表格
        this.typeMapperModel = new TypeMapperModel();
        this.typeMapperTable.setModel(typeMapperModel);
        refresh();
    }

    /**
     * 刷新方法
     */
    private void refresh() {
        init = false;
        //初始化下拉框
        this.typeMapperComboBox.removeAllItems();
        typeMapperGroupMap.keySet().forEach(this.typeMapperComboBox::addItem);
        this.typeMapperComboBox.setSelectedItem(this.currGroupName);
        this.typeMapperModel.init(this.typeMapperGroupMap.get(currGroupName).getElementList());
        init = true;
    }

    @Override
    public String getDisplayName() {
        return "Type Mapper";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        return mainPanel;
    }

    @Override
    public boolean isModified() {
        return !typeMapperGroupMap.equals(settings.getTypeMapperGroupMap()) || !currGroupName.equals(settings.getCurrTypeMapperGroupName());
    }

    @Override
    public void apply() {
        settings.setCurrTypeMapperGroupName(currGroupName);
        settings.setTypeMapperGroupMap(typeMapperGroupMap);
    }

    @Override
    public void reset() {
        // 配置服务实例化, 重新加载，因为数据发生变更
        this.settings = ConfigService.getInstance(this.project);
        this.typeMapperGroupMap = CloneUtils.cloneByJson(settings.getTypeMapperGroupMap(), new TypeReference<Map<String, TypeMapperGroup>>() {});
        this.currGroupName = settings.getCurrTypeMapperGroupName();
        init();
    }
}
