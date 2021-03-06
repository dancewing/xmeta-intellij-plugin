package io.xmeta.generator.vo.impl;

import io.xmeta.api.data.MetaEntity;
import io.xmeta.generator.config.OutputSettings;
import io.xmeta.generator.vo.IEntity;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;

import java.util.Set;

/**
 * 实体类信息
 *
 * @author HouKunLin
 * @date 2020/5/28 0028 0:59
 */
@Getter
public class EntityImpl implements IEntity {
    private final EntityPackage packages = new EntityPackage();
    private EntityName name;
    @Setter
    private String comment;
    @Setter
    private String uri;

    public EntityImpl(MetaEntity dbTable) {
        this.comment = StringUtils.defaultString(dbTable.getName(), "");
        this.name = new EntityName(dbTable);
    }

    public void setName(String name) {
        this.name = new EntityName(name);
    }

    /**
     * 初始化更多的信息
     *
     * @param fullTypeNames 字段类型名称列表
     * @param outputSettings      设置信息对象（用来初始化包名信息）
     */
    public void initMore(Set<String> fullTypeNames, OutputSettings outputSettings) {
        packages.clear();
        fullTypeNames.forEach(packages::add);
        name.initMore(outputSettings);
        packages.initMore(outputSettings, name);
    }
}
