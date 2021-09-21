package com.github.houkunlin.ui.win;

import com.github.houkunlin.config.OutputSettings;
import com.github.houkunlin.util.PluginUtils;
import com.google.common.collect.Maps;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaCodeFragment;
import com.intellij.psi.JavaCodeFragmentFactory;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.ui.EditorTextField;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/**
 * 基础设置窗口
 *
 * @author HouKunLin
 * @date 2020/8/15 0015 16:00
 */
public class PackageSetting implements IWindows {
    /**
     * 配置对象：设置信息
     */
    private final OutputSettings outputSettings;

    /**
     * 输入框：Entity前缀
     */
    private JTextField entitySuffixField;
    /**
     * 输入框：Entity包名
     */
    private EditorTextField entityPackageField;

    /**
     * 输入框：Dao前缀
     */
    private JTextField daoSuffixField;
    /**
     * 输入框：Dao包名
     */
    private EditorTextField daoPackageField;

    /**
     * 输入框：XML包名
     */
    private JTextField xmlPackageField;
    /**
     * 输入框：Service前缀
     */
    private JTextField serviceSuffixField;
    /**
     * 输入框：Service包名
     */
    private EditorTextField servicePackageField;

    /**
     * 输入框：Controller前缀
     */
    private JTextField controllerSuffixField;
    /**
     * 输入框：Controller包名
     */
    private EditorTextField controllerPackageField;


    /**
     * 面板：顶级页面面板
     */
    private JPanel content;

    public PackageSetting(OutputSettings outputSettings) {
        this.outputSettings = outputSettings;
        initConfig();

        /* 普通输入框的输入事件监听 */
        class TextFieldDocumentListener implements DocumentListener {
            /**
             * 存放 setValue 与 输入框 的关系
             */
            private final Map<Consumer<String>, JTextComponent> map = Maps.newHashMap();

            public TextFieldDocumentListener() {
                map.put(outputSettings::setEntitySuffix, entitySuffixField);
                map.put(outputSettings::setDaoSuffix, daoSuffixField);
                map.put(outputSettings::setServiceSuffix, serviceSuffixField);
                map.put(outputSettings::setControllerSuffix, controllerSuffixField);
                map.put(outputSettings::setXmlPackage, xmlPackageField);
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
        entitySuffixField.getDocument().addDocumentListener(textFieldDocumentListener);
        daoSuffixField.getDocument().addDocumentListener(textFieldDocumentListener);
        serviceSuffixField.getDocument().addDocumentListener(textFieldDocumentListener);
        controllerSuffixField.getDocument().addDocumentListener(textFieldDocumentListener);
        xmlPackageField.getDocument().addDocumentListener(textFieldDocumentListener);

        /* 包名输入框的输入事件监听 */
        class EditorTextFieldDocumentListener implements com.intellij.openapi.editor.event.DocumentListener {
            /**
             * 存放 setValue 与 输入框 的关系
             */
            private final Map<Consumer<String>, EditorTextField> map = Maps.newHashMap();

            public EditorTextFieldDocumentListener() {
                map.put(outputSettings::setEntitySubPackage, entityPackageField);
                map.put(outputSettings::setDaoSubPackage, daoPackageField);
                map.put(outputSettings::setServiceSubPackage, servicePackageField);
                map.put(outputSettings::setControllerSubPackage, controllerPackageField);
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
        entityPackageField.getDocument().addDocumentListener(editorTextFieldDocumentListener);
        daoPackageField.getDocument().addDocumentListener(editorTextFieldDocumentListener);
        servicePackageField.getDocument().addDocumentListener(editorTextFieldDocumentListener);
        controllerPackageField.getDocument().addDocumentListener(editorTextFieldDocumentListener);
    }

    private void createUIComponents() {
        Project project = PluginUtils.getProject();
        entityPackageField = createEditorTextField(project);
        daoPackageField = createEditorTextField(project);
        servicePackageField = createEditorTextField(project);
        controllerPackageField = createEditorTextField(project);
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
     * 初始化开发者信息的输入框内容
     */
    private void initConfig() {

        entitySuffixField.setText(outputSettings.getEntitySuffix());
        daoSuffixField.setText(outputSettings.getDaoSuffix());
        serviceSuffixField.setText(outputSettings.getServiceSuffix());
        controllerSuffixField.setText(outputSettings.getControllerSuffix());

        entityPackageField.setText(outputSettings.getEntitySubPackage());
        daoPackageField.setText(outputSettings.getDaoSubPackage());
        servicePackageField.setText(outputSettings.getServiceSubPackage());
        controllerPackageField.setText(outputSettings.getControllerSubPackage());
        xmlPackageField.setText(outputSettings.getXmlPackage());

    }

    @Override
    public JPanel getContent() {
        return content;
    }
}
