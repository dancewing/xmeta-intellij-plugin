package com.github.houkunlin.util;

import com.github.houkunlin.config.ConfigService;
import com.github.houkunlin.model.Template;
import com.intellij.openapi.project.Project;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public final class TemplateFileUtils {

    public static  void copyTemplateGroup(Project project, String source, String dest){
        ConfigService configService = ConfigService.getInstance(project);
        File configRootPath = configService.getConfigRootPath();
        File templateFolder = new File(configRootPath, PluginUtils.TEMPLATE_DIR);
        File sourceFolder = new File(templateFolder, source);
        File destFolder = new File(templateFolder, dest);
        destFolder.mkdirs();
        try {
            FileUtils.copyDirectory(sourceFolder, destFolder);
            if (StringUtils.contains(destFolder.getAbsolutePath(), project.getBasePath())) {
                PluginUtils.refreshProject(destFolder.getAbsolutePath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String loadTemplateCode(Project project, String groupName, String templateName){
        ConfigService configService = ConfigService.getInstance(project);
        File configRootPath = configService.getConfigRootPath();
        File templateFolder = new File(configRootPath, PluginUtils.TEMPLATE_DIR);
        File groupFolder = new File(templateFolder, groupName);
        File destFile = new File(groupFolder, templateName);
        try {
            return FileUtils.readFileToString(destFile, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void saveTemplateCode(Project project, String groupName, Template template, String content){
        ConfigService configService = ConfigService.getInstance(project);
        File configRootPath = configService.getConfigRootPath();
        File templateFolder = new File(configRootPath, PluginUtils.TEMPLATE_DIR);
        File groupFolder = new File(templateFolder, groupName);
        File destFile = new File(groupFolder, String.valueOf(template.getMode().name()) + "/"+ template.getName());
        try {
            FileUtils.writeStringToFile(destFile, content, StandardCharsets.UTF_8);
            if (StringUtils.contains(destFile.getAbsolutePath(), project.getBasePath())) {
                PluginUtils.refreshProject(groupFolder.getAbsolutePath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadTemplateCode(Template template) {

    }
}
