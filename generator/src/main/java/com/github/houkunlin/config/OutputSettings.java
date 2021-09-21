package com.github.houkunlin.config;

import lombok.Data;

/**
 * 设置信息
 *
 * @author HouKunLin
 * @date 2020/4/3 0003 14:56
 */
@Data
public class OutputSettings {
    /**
     * 项目路径
     */
    private String projectPath;
    /**
     * Java代码路径
     */
    private String javaPath = "src/main/java";
    /**
     * 资源文件路径
     */
    private String sourcesPath = "src/main/resources";
    /**
     * Entity 后缀
     */
    private String entitySuffix = "Entity";
    /**
     * Dao 后缀
     */
    private String daoSuffix = "Repository";
    /**
     * Service 后缀
     */
    private String serviceSuffix = "Service";
    /**
     * Controller 后缀
     */
    private String controllerSuffix = "Controller";
    /**
     * Entity 包名
     */
    private String entitySubPackage = "entity";
    /**
     * Dao 包名
     */
    private String daoSubPackage = "repository";
    /**
     * Service 包名
     */
    private String serviceSubPackage = "service";
    /**
     * Controller 包名
     */
    private String controllerSubPackage = "controller";
    /**
     * Mapper XML 包名
     */
    private String xmlPackage = "mapper";

    private String rootPackage = "com.test";

    public String getDaoPackage() {
        return rootPackage + "." + daoSubPackage;
    }

    public String getEntityPackage () {
        return rootPackage + "." + entitySubPackage;
    }

    public String getServicePackage () {
        return rootPackage + "." + serviceSubPackage;
    }

    public String getControllerPackage () {
        return rootPackage + "." + controllerSubPackage;
    }

    public String getSourcesPathAt(String filename) {
        return sourcesPath + "/" + filename;
    }

    public String getJavaPathAt(String filename) {
        return javaPath + "/" + filename;
    }
}
