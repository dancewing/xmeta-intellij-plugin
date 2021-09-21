package io.xmeta.generator.vo.impl;

import io.xmeta.generator.vo.ITable;
import io.xmeta.api.data.GraphEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang.StringUtils;

/**
 * 数据库表信息
 *
 * @author HouKunLin
 * @date 2020/5/28 0028 0:59
 */
@Getter
public class TableImpl implements ITable {
    /**
     * 数据库表的原始对象
     */
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private final GraphEntity dbTable;

    private final String name;
    private final String comment;

    public TableImpl(GraphEntity dbTable) {
        this.dbTable = dbTable;
        this.name = dbTable.getName();
        this.comment = StringUtils.defaultString(dbTable.getName(), "");
    }
}
