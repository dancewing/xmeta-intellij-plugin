package io.xmeta.generator.model;

import io.xmeta.generator.config.BaseSettings;
import io.xmeta.generator.config.OutputSettings;
import io.xmeta.generator.vo.Variable;
import io.xmeta.generator.vo.impl.RootModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 保存文件信息
 *
 * @author HouKunLin
 * @date 2020/4/3 0003 20:52
 */
public class SaveFilePath {
    /**
     * 模板 gen 指令允许的 type 值类型
     */
    private static final List<String> types = new ArrayList<>();

    static {
        types.add("entity");
        types.add("dao");
        types.add("service");
        types.add("serviceImpl");
        types.add("controller");
        types.add("xml");
    }

    /**
     * 模板文件的 type 类型
     */
    private String type;
    /**
     * 模板文件保存路径
     */
    private String toString;

    public SaveFilePath(String defaultFilename, String defaultFilepath) {
        String name = getValue(Variable.filename, defaultFilename);
        String path = getValue(Variable.filepath, defaultFilepath);
        type = Variable.type;
        toString = (path.replace(".", "/") + "/" + name);
        toString = toString.replace("\\", "/").replaceAll("/+", "/");
        Variable.resetVariables();
    }

    public static void resetVariables() {
        Variable.filename = null;
        Variable.filepath = null;
        Variable.type = null;
    }

    public static SaveFilePath create(RootModel rootModel, OutputSettings outputSettings) {
        SaveFilePath saveFilePath;
        String entityName = String.valueOf(rootModel.getEntity().getName());
        if (Variable.type == null) {
            return new SaveFilePath(entityName + ".java",
                    outputSettings.getSourcesPathAt("temp"));
        }
        switch (Variable.type) {
            case "entity":
                saveFilePath = new SaveFilePath(entityName + outputSettings.getEntitySuffix() + ".java",
                        outputSettings.getJavaPathAt(outputSettings.getEntityPackage()));
                break;
            case "dao":
                saveFilePath = new SaveFilePath(entityName + outputSettings.getDaoSuffix() + ".java",
                        outputSettings.getJavaPathAt(outputSettings.getDaoPackage()));
                break;
            case "service":
                saveFilePath = new SaveFilePath(entityName + outputSettings.getServiceSuffix() + ".java",
                        outputSettings.getJavaPathAt(outputSettings.getServicePackage()));
                break;
            case "serviceImpl":
                saveFilePath = new SaveFilePath(entityName + outputSettings.getServiceSuffix() + "Impl.java",
                        outputSettings.getJavaPathAt(outputSettings.getServicePackage() + ".impl"));
                break;
            case "controller":
                saveFilePath = new SaveFilePath(entityName + outputSettings.getControllerSuffix() + ".java",
                        outputSettings.getJavaPathAt(outputSettings.getControllerPackage()));
                break;
            case "xml":
                saveFilePath = new SaveFilePath(entityName + outputSettings.getDaoSuffix() + ".xml",
                        outputSettings.getSourcesPathAt(outputSettings.getXmlPackage()));
                break;
            default:
                saveFilePath = new SaveFilePath(entityName + ".java",
                        outputSettings.getSourcesPathAt("temp"));
        }
        return saveFilePath;
    }

    private String getValue(String tempValue, String defaultValue) {
        if (tempValue != null) {
            return tempValue;
        } else {
            return defaultValue;
        }
    }

    /**
     * 判断是否是某种类型的文件
     *
     * @param type 文件类型
     * @return 结果
     */
    public boolean isType(String type) {
        if (this.type == null) {
            return false;
        }
        return this.type.equals(type);
    }

    public boolean isEntity() {
        return "entity".equals(type);
    }

    public boolean isDao() {
        return "dao".equals(type);
    }

    public boolean isService() {
        return "service".equals(type);
    }

    public boolean isServiceImpl() {
        return "serviceImpl".equals(type);
    }

    public boolean isController() {
        return "controller".equals(type);
    }

    public boolean isJava() {
        return types.contains(type) && !isXml();
    }

    public boolean isXml() {
        return "xml".equals(type);
    }

    public boolean isOther() {
        return !types.contains(type);
    }

    public boolean isOverride(BaseSettings options) {
        boolean isOverride = false;
        if (options.isOverrideJava() && isJava()) {
            isOverride = true;
        } else if (options.isOverrideXml() && isXml()) {
            isOverride = true;
        } else if (options.isOverrideOther() && isOther()) {
            isOverride = true;
        }
        return isOverride;
    }

    @Override
    public String toString() {
        return toString;
    }
}
