package io.xmeta.generator.vo.impl;

import io.xmeta.api.data.MetaEntity;
import io.xmeta.generator.config.OutputSettings;
import io.xmeta.generator.vo.IName;
import com.google.common.base.CaseFormat;
import lombok.Getter;

/**
 * 实体类名称对象。提供方便直接获取 Entity、Service、ServiceImpl、Dao、Controller 的对象完整名称
 *
 * @author HouKunLin
 * @date 2020/7/5 0005 15:07
 */
@Getter
public class EntityName implements IName {
    private final String value;
    private final String firstUpper;
    private final String firstLower;
    /**
     * 实体类完整名称
     */
    private IName entity;

    /**
     * Domain完整名称
     */
    private IName domain;

    /**
     * Mapper完整名称
     */
    private IName mapper;

    /**
     * Service 完整名称
     */
    private IName service;
    /**
     * ServiceImpl 完整名称
     */
    private IName serviceImpl;
    /**
     * Dao 完整名称
     */
    private IName dao;
    /**
     * Controller 完整名称
     */
    private IName controller;

    public EntityName(MetaEntity dbTable) {
        this.value = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, dbTable.getName());
        this.firstUpper = value;
        this.firstLower = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, value);
    }

    public EntityName(String name) {
        this.value = name;
        this.firstUpper = value;
        this.firstLower = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, value);
    }

    @Override
    public String toString() {
        return value;
    }

    public void initMore(OutputSettings outputSettings) {
        this.entity = build(outputSettings.getServer().getEntitySuffix());
        this.service = build(outputSettings.getServer().getServiceSuffix());
        this.serviceImpl = build(outputSettings.getServer().getServiceSuffix() + "Impl");
        this.dao = build(outputSettings.getServer().getDaoSuffix());
        this.controller = build(outputSettings.getServer().getControllerSuffix());
        this.domain = build(outputSettings.getServer().getDomainSuffix());
        this.mapper = build(outputSettings.getServer().getMapperSuffix());
    }

    private IName build(String suffix) {
        return new EntityNameInfo(value, suffix);
    }
}
