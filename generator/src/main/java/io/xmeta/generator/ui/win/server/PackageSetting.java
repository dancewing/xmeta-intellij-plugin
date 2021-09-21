package io.xmeta.generator.ui.win.server;

import io.xmeta.generator.config.OutputSettings;
import io.xmeta.generator.ui.win.IWindows;
import io.xmeta.generator.ui.win.TextFieldDocumentUtil;
import io.xmeta.generator.util.PluginUtils;
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
     * 输入框：Entity前缀
     */
    private JTextField domainSuffixField;
    /**
     * 输入框：Entity包名
     */
    private EditorTextField domainPackageField;

    /**
     * 输入框：Dao前缀
     */
    private JTextField mapperSuffixField;
    /**
     * 输入框：Dao包名
     */
    private EditorTextField mapperPackageField;

    /**
     * 输入框：Dao前缀
     */
    private JTextField daoSuffixField;
    /**
     * 输入框：Dao包名
     */
    private EditorTextField daoPackageField;

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
                map.put(outputSettings.getServer()::setEntitySuffix, entitySuffixField);
                map.put(outputSettings.getServer()::setDaoSuffix, daoSuffixField);
                map.put(outputSettings.getServer()::setServiceSuffix, serviceSuffixField);
                map.put(outputSettings.getServer()::setControllerSuffix, controllerSuffixField);
                map.put(outputSettings.getServer()::setDomainSubPackage, domainSuffixField);
                map.put(outputSettings.getServer()::setMapperSubPackage, mapperSuffixField);
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
        domainSuffixField.getDocument().addDocumentListener(textFieldDocumentListener);
        mapperSuffixField.getDocument().addDocumentListener(textFieldDocumentListener);

        /* 包名输入框的输入事件监听 */
        class EditorTextFieldDocumentListener implements com.intellij.openapi.editor.event.DocumentListener {
            /**
             * 存放 setValue 与 输入框 的关系
             */
            private final Map<Consumer<String>, EditorTextField> map = Maps.newHashMap();

            public EditorTextFieldDocumentListener() {
                map.put(outputSettings.getServer()::setEntitySubPackage, entityPackageField);
                map.put(outputSettings.getServer()::setDaoSubPackage, daoPackageField);
                map.put(outputSettings.getServer()::setServiceSubPackage, servicePackageField);
                map.put(outputSettings.getServer()::setControllerSubPackage, controllerPackageField);
                map.put(outputSettings.getServer()::setDomainSubPackage, domainPackageField);
                map.put(outputSettings.getServer()::setMapperSubPackage, mapperPackageField);
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
        domainPackageField.getDocument().addDocumentListener(editorTextFieldDocumentListener);
        mapperPackageField.getDocument().addDocumentListener(editorTextFieldDocumentListener);
    }

    private void createUIComponents() {
        Project project = PluginUtils.getProject();
        entityPackageField = createEditorTextField(project);
        daoPackageField = createEditorTextField(project);
        servicePackageField = createEditorTextField(project);
        controllerPackageField = createEditorTextField(project);
        domainPackageField = createEditorTextField(project);
        mapperPackageField = createEditorTextField(project);
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

        entitySuffixField.setText(outputSettings.getServer().getEntitySuffix());
        daoSuffixField.setText(outputSettings.getServer().getDaoSuffix());
        serviceSuffixField.setText(outputSettings.getServer().getServiceSuffix());
        controllerSuffixField.setText(outputSettings.getServer().getControllerSuffix());
        domainSuffixField.setText(outputSettings.getServer().getDomainSuffix());
        mapperSuffixField.setText(outputSettings.getServer().getMapperSuffix());


        entityPackageField.setText(outputSettings.getServer().getEntitySubPackage());
        daoPackageField.setText(outputSettings.getServer().getDaoSubPackage());
        servicePackageField.setText(outputSettings.getServer().getServiceSubPackage());
        controllerPackageField.setText(outputSettings.getServer().getControllerSubPackage());
        domainPackageField.setText(outputSettings.getServer().getDomainSubPackage());
        mapperPackageField.setText(outputSettings.getServer().getMapperSubPackage());

    }

    @Override
    public JPanel getContent() {
        return content;
    }
}
