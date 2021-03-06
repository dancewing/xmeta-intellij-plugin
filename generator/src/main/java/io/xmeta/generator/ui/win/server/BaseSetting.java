package io.xmeta.generator.ui.win.server;

import io.xmeta.generator.config.BaseSettings;
import io.xmeta.generator.config.OutputSettings;
import io.xmeta.generator.ui.win.IWindows;
import io.xmeta.generator.ui.win.TextFieldDocumentUtil;
import io.xmeta.generator.util.PluginUtils;
import com.google.common.collect.Maps;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.ide.util.PackageChooserDialog;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaCodeFragment;
import com.intellij.psi.JavaCodeFragmentFactory;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiPackage;
import com.intellij.ui.EditorTextField;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/**
 * 基础设置窗口
 *
 * @author HouKunLin
 * @date 2020/8/15 0015 16:00
 */
public class BaseSetting implements IWindows {
    /**
     * 配置对象：设置信息
     */
    private final OutputSettings outputSettings;
    /**
     * 配置对象：开发者信息
     */
    private final BaseSettings baseSettings;

    /**
     * 输入框：Root package 前缀
     */
    private EditorTextField rootPackageField;

    /**
     * 按钮：Root package 包选择
     */
    private JButton selectRootPackageButton;

    /**
     * 输入框：开发者姓名
     */
    private JTextField authorField;
    /**
     * 输入框：电子邮件
     */
    private JTextField emailField;
    /**
     * 面板：顶级页面面板
     */
    private JPanel content;
    /**
     * 复选：是否覆盖Java文件
     */
    private JCheckBox overrideJavaCheckBox;
    /**
     * 复选：是否覆盖XML文件
     */
    private JCheckBox overrideXmlCheckBox;
    /**
     * 复选：是否覆盖其他文件
     */
    private JCheckBox overrideOtherCheckBox;

    public BaseSetting(OutputSettings outputSettings, BaseSettings baseSettings) {
        this.outputSettings = outputSettings;
        this.baseSettings = baseSettings;
        initConfig();
        configSelectPackage();

        /* 普通输入框的输入事件监听 */
        class TextFieldDocumentListener implements DocumentListener {
            /**
             * 存放 setValue 与 输入框 的关系
             */
            private final Map<Consumer<String>, JTextComponent> map = Maps.newHashMap();

            public TextFieldDocumentListener() {
                map.put(baseSettings::setAuthor, authorField);
                map.put(baseSettings::setEmail, emailField);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                documentChanged(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                documentChanged(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }

            /**
             * swing 输入框组件内容更改事件
             *
             * @param e 事件
             */
            public void documentChanged(DocumentEvent e) {
                javax.swing.text.Document document = e.getDocument();
                Set<Map.Entry<Consumer<String>, JTextComponent>> entries = map.entrySet();
                for (Map.Entry<Consumer<String>, JTextComponent> entry : entries) {
                    if (TextFieldDocumentUtil.updateSettingValue(document, entry.getValue(), entry.getKey())) {
                        break;
                    }
                }
            }
        }

        TextFieldDocumentListener textFieldDocumentListener = new TextFieldDocumentListener();
        authorField.getDocument().addDocumentListener(textFieldDocumentListener);
        emailField.getDocument().addDocumentListener(textFieldDocumentListener);

        /* 包名输入框的输入事件监听 */
        class EditorTextFieldDocumentListener implements com.intellij.openapi.editor.event.DocumentListener {
            /**
             * 存放 setValue 与 输入框 的关系
             */
            private final Map<Consumer<String>, EditorTextField> map = Maps.newHashMap();

            public EditorTextFieldDocumentListener() {
                map.put(outputSettings.getServer()::setRootPackage, rootPackageField);
            }

            /**
             * IDEA 输入框组件内容更改事件
             *
             * @param event 事件
             */
            @Override
            public void documentChanged(@NotNull com.intellij.openapi.editor.event.DocumentEvent event) {
                Document document = event.getDocument();
                Set<Map.Entry<Consumer<String>, EditorTextField>> entries = map.entrySet();
                for (Map.Entry<Consumer<String>, EditorTextField> entry : entries) {
                    if (TextFieldDocumentUtil.updateSettingValue(document, entry.getValue(), entry.getKey())) {
                        break;
                    }
                }
            }
        }

        EditorTextFieldDocumentListener editorTextFieldDocumentListener = new EditorTextFieldDocumentListener();
        rootPackageField.getDocument().addDocumentListener(editorTextFieldDocumentListener);

        ItemListener checkBoxItemListener = new ItemListener() {
            /**
             * 复选框勾选事件监听
             * @param e 事件
             */
            @Override
            public void itemStateChanged(ItemEvent e) {
                Object item = e.getItem();
                if (overrideJavaCheckBox == item) {
                    baseSettings.setOverrideJava(overrideJavaCheckBox.isSelected());
                } else if (overrideXmlCheckBox == item) {
                    baseSettings.setOverrideXml(overrideXmlCheckBox.isSelected());
                } else if (overrideOtherCheckBox == item) {
                    baseSettings.setOverrideOther(overrideOtherCheckBox.isSelected());
                }
            }
        };
        overrideJavaCheckBox.addItemListener(checkBoxItemListener);
        overrideXmlCheckBox.addItemListener(checkBoxItemListener);
        overrideOtherCheckBox.addItemListener(checkBoxItemListener);
    }

    private void createUIComponents() {
        Project project = PluginUtils.getProject();
        rootPackageField = createEditorTextField(project);
    }

    /**
     * 创建一个可以自动补全包名的输入框
     *
     * @param project 项目
     * @return IDEA 输入框组件
     */
    private EditorTextField createEditorTextField(Project project) {
        // https://jetbrains.org/intellij/sdk/docs/user_interface_components/editor_components.html
        JavaCodeFragment code = JavaCodeFragmentFactory.getInstance(project).createReferenceCodeFragment("", null, true, false);
        Document document = PsiDocumentManager.getInstance(project).getDocument(code);
        JavaFileType fileType = JavaFileType.INSTANCE;
        return new EditorTextField(document, project, fileType);
    }

    /**
     * 配置选择包名的按钮事件
     */
    private void configSelectPackage() {
        selectRootPackageButton.addActionListener(e -> {
            chooserPackage(rootPackageField.getText(), rootPackageField::setText);
        });
    }

    /**
     * 选择包名
     *
     * @param defaultSelect 默认选中包名
     * @param consumer      完成事件
     */
    private void chooserPackage(String defaultSelect, Consumer<String> consumer) {
        PackageChooserDialog chooser = new PackageChooserDialog("请选择模块包", PluginUtils.getProject());
        chooser.selectPackage(defaultSelect);
        chooser.show();
        PsiPackage psiPackage = chooser.getSelectedPackage();
        if (psiPackage != null) {
            consumer.accept(psiPackage.getQualifiedName());
        }
        chooser.getDisposable().dispose();
    }

    /**
     * 初始化开发者信息的输入框内容
     */
    private void initConfig() {
        authorField.setText(baseSettings.getAuthor());
        emailField.setText(baseSettings.getEmail());
        rootPackageField.setText(outputSettings.getServer().getRootPackage());
        overrideJavaCheckBox.setSelected(baseSettings.isOverrideJava());
        overrideXmlCheckBox.setSelected(baseSettings.isOverrideXml());
        overrideOtherCheckBox.setSelected(baseSettings.isOverrideOther());
    }

    @Override
    public JPanel getContent() {
        return content;
    }
}
