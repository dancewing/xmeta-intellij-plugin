package io.xmeta.generator.vo.impl;

import io.xmeta.generator.config.OutputSettings;
import lombok.Getter;
import org.apache.commons.lang.StringUtils;

import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * 实体类对象的包信息
 *
 * @author HouKunLin
 * @date 2020/7/5 0005 15:12
 */
@Getter
public class EntityPackage {
    /**
     * 实体类字段所需要导入的包列表
     */
    private final HashSet<String> list = new HashSet<>();
    private String toString = "";
    /**
     * 实体类包名信息
     */
    private EntityPackageInfo entity;
    /**
     * Service 包名信息
     */
    private EntityPackageInfo service;
    /**
     * ServiceImpl 包名信息
     */
    private EntityPackageInfo serviceImpl;
    /**
     * Dao 包名信息
     */
    private EntityPackageInfo dao;
    /**
     * Controller 包名信息
     */
    private EntityPackageInfo controller;

    public void add(String fullPackageName) {
        if (fullPackageName.startsWith("java.lang.")) {
            return;
        }
        list.add(fullPackageName);
    }

    public void clear() {
        list.clear();
        toString = "";
    }

    @Override
    public String toString() {
        if (StringUtils.isBlank(toString)) {
            toString = list.stream().map(item -> String.format("import %s;\n", item)).collect(Collectors.joining());
        }
        return toString;
    }

    public void initMore(OutputSettings outputSettings, EntityName entityName) {
        this.entity = new EntityPackageInfo(outputSettings.getEntityPackage(), entityName.getEntity());
        this.service = new EntityPackageInfo(outputSettings.getServicePackage(), entityName.getService());
        this.serviceImpl = new EntityPackageInfo(outputSettings.getServicePackage() + ".impl", entityName.getServiceImpl());
        this.dao = new EntityPackageInfo(outputSettings.getDaoPackage(), entityName.getDao());
        this.controller = new EntityPackageInfo(outputSettings.getControllerPackage(), entityName.getController());
    }
}
