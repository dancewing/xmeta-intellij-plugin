package io.xmeta.generator.ui.win.server;

import io.xmeta.generator.config.BaseSettings;
import io.xmeta.generator.config.ConfigService;
import io.xmeta.generator.config.OutputSettings;
import io.xmeta.generator.model.GenMode;
import io.xmeta.generator.task.GeneratorTask;
import io.xmeta.generator.ui.win.TextFieldDocumentUtil;
import io.xmeta.generator.util.Generator;
import io.xmeta.generator.util.PluginUtils;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.TextBrowseFolderListener;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import io.xmeta.api.data.MetaEntity;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.io.File;
import java.util.List;

public class MainDialog extends DialogWrapper {
    /**
     * 配置对象
     */
    private final ConfigService configService;
    /**
     * 配置对象：设置信息
     */
    private final OutputSettings outputSettings;
    /**
     * 配置对象：开发者信息
     */
    private final BaseSettings baseSettings;
    /**
     * 面板对象：数据库表配置
     */
    private final TableSetting tableSetting;
    /**
     * 面板对象：基础信息配置
     */
    private final BaseSetting baseSetting;

    private final PackageSetting packageSetting;

    /**
     * 面板对象：模板选择配置
     */
    private final SelectTemplate selectTemplate;
    /**
     * 输入框：项目路径输入、选择
     */
    private TextFieldWithBrowseButton projectPathField;
    /**
     * 输入框：Java代码路径
     */
    private JTextField javaPathField;
    /**
     * 输入框：资源代码路径
     */
    private JTextField sourcesPathField;

    /**
     * 输入框：输入内容监听
     */
    private final DocumentListener documentListener = new DocumentListener() {

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
            if (!TextFieldDocumentUtil.updateSettingValue(document, javaPathField, outputSettings.getServer()::setJavaPath)) {
                if (!TextFieldDocumentUtil.updateSettingValue(document, sourcesPathField,
                        outputSettings.getServer()::setSourcesPath)) {
                    TextFieldDocumentUtil.updateSettingValue(document, projectPathField.getTextField(), outputSettings::setProjectPath);
                }
            }
        }
    };
    /**
     * 内容Tab标签
     */
    private JTabbedPane tableTabbedPane;
    /**
     * 面板：内容面板
     */
    private JPanel content;

    public MainDialog(Project project, MetaEntity[] psiElements, ConfigService configService) {
        // super("代码生成器");
        super(project);
        this.configService = configService;
        this.outputSettings = configService.getOutputSettings();
        this.baseSettings = configService.getBaseSettings();
        baseSetting = new BaseSetting(outputSettings, baseSettings);
        packageSetting = new PackageSetting(outputSettings);
        selectTemplate = new SelectTemplate(project, GenMode.Server);
        tableSetting = new TableSetting(psiElements);
        tableTabbedPane.addTab("基础配置", baseSetting.getContent());
        tableTabbedPane.addTab("包路径配置", packageSetting.getContent());
        tableTabbedPane.addTab("模板选择", selectTemplate.getContent());
        tableTabbedPane.addTab("数据库表配置", tableSetting.getContent());
    }

    /**
     * 初始化窗口配置
     */
    private void initWindows() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//        setContentPane(content);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        int frameWidth = content.getPreferredSize().width;
        int frameHeight = content.getPreferredSize().height;
        setLocation((screenSize.width - frameWidth) / 2, (screenSize.height - frameHeight) / 2);
        setResizable(false);
        pack();
        //setVisible(true);
    }

    /**
     * 初始化配置信息
     */
    private void initConfig() {
        initSettings();
        FileChooserDescriptor chooserDescriptor = FileChooserDescriptorFactory.createSingleFileOrFolderDescriptor();
        chooserDescriptor.setTitle("选择项目路径");
        Project project = PluginUtils.getProject();
        projectPathField.addBrowseFolderListener(new TextBrowseFolderListener(chooserDescriptor, project));
    }

    public GeneratorTask getTask() {
        Project project = PluginUtils.getProject();
        try {
            List<File> allSelectFile = selectTemplate.getAllSelectFile();
            if (allSelectFile.isEmpty()) {
                Messages.showWarningDialog("当前无选中代码模板文件，无法进行代码生成，请选中至少一个代码模板文件！", "警告");
                return null;
            }
            // setVisible(false);
            Generator generator = new Generator(outputSettings, baseSettings);
            GeneratorTask generatorTask = new GeneratorTask(project, this, generator, allSelectFile, tableSetting.getRootModels());
            return generatorTask;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            //   setVisible(true);
            Messages.showErrorDialog("初始化代码生成处理器失败，请联系开发者。\n\n" + throwable.getMessage(), "生成代码失败");
        }
        configService.setBaseSettings(baseSettings);
        configService.setOutputSettings(outputSettings);
        return null;
    }

    /**
     * 初始化设置信息
     */
    private void initSettings() {
        projectPathField.getTextField().getDocument().addDocumentListener(documentListener);
        javaPathField.getDocument().addDocumentListener(documentListener);
        sourcesPathField.getDocument().addDocumentListener(documentListener);

        String projectPath = outputSettings.getProjectPath();
        if (StringUtils.isBlank(projectPath)) {
            projectPath = PluginUtils.getProject().getBasePath();
        }
        projectPathField.setText(projectPath);
        javaPathField.setText(outputSettings.getServer().getJavaPath());
        sourcesPathField.setText(outputSettings.getServer().getSourcesPath());
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return this.content;
    }

    public boolean go() {
        init();
        initWindows();
        initConfig();
        return showAndGet();
    }

    @Override
    protected void doOKAction() {
        //validate
        super.doOKAction();
    }
}
