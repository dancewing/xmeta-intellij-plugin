package com.github.houkunlin.config;

import com.github.houkunlin.model.*;
import com.github.houkunlin.util.PluginUtils;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.util.xmlb.annotations.Transient;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 配置 Service
 *
 * @author HouKunLin
 * @date 2020/8/18 0018 17:17
 */
@Data
@State(name = "com.github.houkunlin.database.generator.config.ConfigService",
        defaultStateAsResource = true,
        storages = {@Storage("database-generator-config.xml")})
public class ConfigService implements PersistentStateComponent<ConfigService> {

    @Transient
    public static final String DEFAULT_NAME = "Default";


    private BaseSettings baseSettings;
    private OutputSettings outputSettings;
    private OtherConfig otherConfig;

    /**
     * 当前类型映射组名
     */
    private String currTypeMapperGroupName;
    /**
     * 类型映射组
     */
    private Map<String, TypeMapperGroup> typeMapperGroupMap;
    /**
     * 当前模板组名
     */
    private String currTemplateGroupName;
    /**
     * 模板组
     */
    private Map<String, TemplateGroup> templateGroupMap;

    /**
     * 当前全局配置组名
     */
    private String currGlobalConfigGroupName;

    /**
     * 当前配置表组名
     */
    private String currColumnConfigGroupName;

    /**
     * 全局配置组
     */
    private Map<String, GlobalConfigGroup> globalConfigGroupMap;

    /**
     * 配置表组
     */
    private Map<String, ColumnConfigGroup> columnConfigGroupMap;

    public ConfigService() {
        baseSettings = PluginUtils.getConfig(BaseSettings.class);
        outputSettings = PluginUtils.getConfig(OutputSettings.class);
        otherConfig = new OtherConfig();
        assert baseSettings != null;
        assert outputSettings != null;

        // 当前各项分组名称
        this.currTemplateGroupName = DEFAULT_NAME;
        this.currTypeMapperGroupName = DEFAULT_NAME;
        this.currColumnConfigGroupName = DEFAULT_NAME;
        this.currGlobalConfigGroupName = DEFAULT_NAME;
        //配置默认模板
        if (this.templateGroupMap == null) {
            this.templateGroupMap = new LinkedHashMap<>();
        }
//        this.templateGroupMap.put(DEFAULT_NAME, loadTemplateGroup(DEFAULT_NAME, "entity.java", "dao.java", "service.java", "serviceImpl.java", "controller.java", "mapper.xml", "debug.json"));
//        this.templateGroupMap.put("MybatisPlus", loadTemplateGroup("MybatisPlus", "entity", "dao", "service", "serviceImpl", "controller"));

        //配置默认类型映射
        if (this.typeMapperGroupMap == null) {
            this.typeMapperGroupMap = new LinkedHashMap<>();
        }
        TypeMapperGroup typeMapperGroup = new TypeMapperGroup();
        List<TypeMapper> typeMapperList = new ArrayList<>();
        typeMapperList.add(new TypeMapper("varchar(\\(\\d+\\))?", "java.lang.String"));
        typeMapperList.add(new TypeMapper("char(\\(\\d+\\))?", "java.lang.String"));
        typeMapperList.add(new TypeMapper("text", "java.lang.String"));
        typeMapperList.add(new TypeMapper("decimal(\\(\\d+\\))?", "java.lang.Double"));
        typeMapperList.add(new TypeMapper("decimal(\\(\\d+,\\d+\\))?", "java.lang.Double"));
        typeMapperList.add(new TypeMapper("integer", "java.lang.Integer"));
        typeMapperList.add(new TypeMapper("int(\\(\\d+\\))?", "java.lang.Integer"));
        typeMapperList.add(new TypeMapper("int4", "java.lang.Integer"));
        typeMapperList.add(new TypeMapper("int8", "java.lang.Long"));
        typeMapperList.add(new TypeMapper("bigint(\\(\\d+\\))?", "java.lang.Long"));
        typeMapperList.add(new TypeMapper("datetime", "java.util.Date"));
        typeMapperList.add(new TypeMapper("timestamp", "java.util.Date"));
        typeMapperList.add(new TypeMapper("boolean", "java.lang.Boolean"));
        typeMapperGroup.setName(DEFAULT_NAME);
        typeMapperGroup.setElementList(typeMapperList);
        typeMapperGroupMap.put(DEFAULT_NAME, typeMapperGroup);

        //初始化表配置
        if (this.columnConfigGroupMap == null) {
            this.columnConfigGroupMap = new LinkedHashMap<>();
        }
        ColumnConfigGroup columnConfigGroup = new ColumnConfigGroup();
        List<ColumnConfig> columnConfigList = new ArrayList<>();
        columnConfigList.add(new ColumnConfig("disable", ColumnConfigType.BOOLEAN));
        columnConfigGroup.setName(DEFAULT_NAME);
        columnConfigGroup.setElementList(columnConfigList);
        columnConfigGroupMap.put(DEFAULT_NAME, columnConfigGroup);

        //初始化全局配置
        if (this.globalConfigGroupMap == null) {
            this.globalConfigGroupMap = new LinkedHashMap<>();
        }
//        this.globalConfigGroupMap.put(DEFAULT_NAME, loadGlobalConfigGroup(DEFAULT_NAME, "init", "define", "autoImport", "mybatisSupport"));
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
}
