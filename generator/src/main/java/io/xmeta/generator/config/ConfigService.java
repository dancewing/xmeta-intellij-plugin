package io.xmeta.generator.config;

import io.xmeta.generator.model.*;
import io.xmeta.generator.util.JsonUtils;
import io.xmeta.generator.util.PluginUtils;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.util.xmlb.annotations.Transient;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 配置 Service
 *
 * @author HouKunLin
 * @date 2020/8/18 0018 17:17
 */
@Data
@Slf4j
@State(name = "com.github.houkunlin.database.generator.config.ConfigService",
        defaultStateAsResource = true,
        storages = {@Storage("database-generator-config.xml")})
public class ConfigService implements PersistentStateComponent<ConfigService> {

    @Transient
    public static final String DEFAULT_NAME = "Default";


    private BaseSettings baseSettings;
    private OutputSettings outputSettings;

    /**
     * 当前模板组名
     */
    private String currTemplateGroupName;

    /**
     * 当前全局配置组名
     */
    private String currGlobalConfigGroupName;

    /**
     * 当前配置表组名
     */
    private String currColumnConfigGroupName;


    public ConfigService() {
        baseSettings = PluginUtils.getConfig(BaseSettings.class);
        outputSettings = PluginUtils.getConfig(OutputSettings.class);
        assert baseSettings != null;
        assert outputSettings != null;

        // 当前各项分组名称
        this.currTemplateGroupName = DEFAULT_NAME;
        this.currColumnConfigGroupName = DEFAULT_NAME;
        this.currGlobalConfigGroupName = DEFAULT_NAME;
    }

    @Nullable
    public static ConfigService getInstance(Project project) {
        return ServiceManager.getService(project, ConfigService.class);
    }

    @Nullable
    @Override
    public ConfigService getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull ConfigService state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    /**
     *
     * @return
     */
    public File getConfigRootPath() {
        if (this.getBaseSettings().isCustomRootDir()) {
            return new File(this.getBaseSettings().getConfigRootPath());
        } else {
            return PluginUtils.getProjectWorkspacePluginDir();
        }
    }

    private static GlobalConfigGroup loadGlobalConfigGroup(File parent, String groupName) {
        GlobalConfigGroup globalConfigGroup = new GlobalConfigGroup();
        globalConfigGroup.setName(groupName);
        globalConfigGroup.setElementList(new ArrayList<>());
        File root = new File(parent, groupName);
        String rootDir = root.getAbsolutePath();
        Collection<File> files = FileUtils.listFiles(root, new String[]{"vm"}, true);
        files.stream().forEach(file -> {
            String relativePath = StringUtils.substringAfter(file.getAbsolutePath(), rootDir);
            String templateName = file.getName();
            try {
                String content = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
                if (StringUtils.startsWith(relativePath, "Server")) {
                    globalConfigGroup.getElementList().add(new GlobalConfig(templateName, content));
                } else {
                    globalConfigGroup.getElementList().add(new GlobalConfig(templateName, content));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
        return globalConfigGroup;
    }

    private static TemplateGroup loadTemplateGroup(File parent, String groupName) {
        TemplateGroup templateGroup = new TemplateGroup();
        templateGroup.setName(groupName);
        templateGroup.setElementList(new ArrayList<>());
        File root = new File(parent, groupName);
        String rootDir = root.getAbsolutePath();
        Collection<File> files = FileUtils.listFiles(root, new String[]{"vm"}, true);
        files.stream().forEach(file -> {
            String relativePath = StringUtils.substringAfter(file.getAbsolutePath(), rootDir);
            String templateName = file.getName();
            try {
                String content = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
                if (StringUtils.startsWith(relativePath, "/Server")) {
                    templateGroup.getElementList().add(new Template(templateName, content, GenMode.Server));
                } else {
                    templateGroup.getElementList().add(new Template(templateName, content ,GenMode.Client));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
        return templateGroup;
    }

    /**
     * 模板组
     */
    public Map<String, TemplateGroup> getTemplateGroupMap() {
        Map<String, TemplateGroup> templateGroupMap = new HashMap<>();
        File templateDir = new File(getConfigRootPath(), PluginUtils.TEMPLATE_DIR);
        File[] listFiles = templateDir.listFiles();
        if (listFiles!=null) {
            Collection<File> files = Arrays.asList(listFiles);
            files.stream().forEach(file -> {
                if (file.isDirectory()) {
                    String groupName = file.getName();
                    TemplateGroup templateGroup = loadTemplateGroup(templateDir, groupName);
                    templateGroupMap.put(groupName, templateGroup);
                }
            });
        }
        return templateGroupMap;
    }

    /**
     * 全局配置组
     */
    public Map<String, GlobalConfigGroup> getGlobalConfigGroupMap() {
        Map<String, GlobalConfigGroup> configGroupMap = new HashMap<>();
        File templateDir = new File(getConfigRootPath(), PluginUtils.GLOBAL_CONFIG_DIR);
        File[] listFiles = templateDir.listFiles();
        if (listFiles!=null) {
            Collection<File> files = Arrays.asList(listFiles);
            files.stream().forEach(file -> {
                if (file.isDirectory()) {
                    String groupName = file.getName();
                    GlobalConfigGroup templateGroup = loadGlobalConfigGroup(templateDir, groupName);
                    configGroupMap.put(groupName, templateGroup);
                }
            });
        }
        return configGroupMap;
    }


    /**
     * 配置表组
     */
    public Map<String, ColumnConfigGroup> getColumnConfigGroupMap() {
        Map<String, ColumnConfigGroup> configGroupMap = new HashMap<>();
        File templateDir = new File(getConfigRootPath(), PluginUtils.MAPPING_DIR);
        File[] listFiles = templateDir.listFiles();
        if (listFiles!=null) {
            Collection<File> files = Arrays.asList(listFiles);
            files.stream().forEach(file -> {
                if (file.isFile() && !file.isHidden()) {
                    String groupName = FilenameUtils.getBaseName(file.getName());
                    if (StringUtils.endsWith(file.getName(), ".json")) {
                        try {
                            ColumnConfig[] columnConfigs = JsonUtils.parse(ColumnConfig[].class, new FileInputStream(file));
                            ColumnConfigGroup templateGroup = new ColumnConfigGroup();
                            templateGroup.setName(groupName);
                            templateGroup.setElementList(Arrays.asList(columnConfigs));
                            configGroupMap.put(groupName, templateGroup);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
        return configGroupMap;
    }
}
