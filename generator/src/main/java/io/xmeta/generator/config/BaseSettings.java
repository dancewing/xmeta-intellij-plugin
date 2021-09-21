package io.xmeta.generator.config;

import lombok.Data;

/**
 * 开发者信息
 *
 * @author HouKunLin
 * @date 2020/4/6 0006 21:00
 */
@Data
public class BaseSettings {
    /**
     * 开发者姓名
     */
    private String author;
    /**
     * 开发者电子邮件
     */
    private String email;

    /**
     * 默认编码
     */
    private String encode;

    private boolean customRootDir = false;

    private String configRootPath = "";

    /**
     * 覆盖Java文件
     */
    private boolean overrideJava = true;
    /**
     * 覆盖XML文件
     */
    private boolean overrideXml = true;
    /**
     * 覆盖其他文件
     */
    private boolean overrideOther = true;
}
