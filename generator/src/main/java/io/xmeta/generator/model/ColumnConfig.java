package io.xmeta.generator.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 列配置信息
 *
 * @author makejava
 * @version 1.0.0
 * @since 2018/07/17 13:10
 */
@Data
@NoArgsConstructor
public class ColumnConfig {
    /**
     * 类型
     */
    private ColumnConfigType type;

    private String shortName;

    private String longName;

    private UIConfigType uiType;

    /**
     * 可选值，逗号分割
     */
    private List<UIConfigType> supportTypes;

    public ColumnConfig(ColumnConfigType type, Class javaType) {
        this.type = type;
        this.longName = javaType.getName();
        this.shortName = javaType.getSimpleName();
    }
}
