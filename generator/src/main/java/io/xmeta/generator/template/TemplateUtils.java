package io.xmeta.generator.template;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * 模板工具
 *
 * @author HouKunLin
 * @date 2020/5/28 0028 1:35
 */
public class TemplateUtils {
    private final VelocityUtils velocityUtils;

    public TemplateUtils(File templateRootPath) throws IOException {
        this.velocityUtils = new VelocityUtils(templateRootPath);
    }

    /**
     * 渲染模板
     *
     * @param templateFile 模板内容
     * @param model        变量信息
     * @return 渲染结果
     * @throws IOException IO异常
     */
    public String generatorFileToString(String templateFile, Map<String, Object> model) throws Exception {
        return velocityUtils.generatorFileToString(templateFile, model);
    }
}
