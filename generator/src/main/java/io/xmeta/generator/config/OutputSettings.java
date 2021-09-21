package io.xmeta.generator.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private Server server = new Server();

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Server {
        /**
         * Java代码路径
         */
        private String javaPath = "src/main/java";
        /**
         * 资源文件路径
         */
        private String sourcesPath = "src/main/resources";

        /**
         * Domain 后缀
         */
        private String domainSuffix = "Domain";

        /**
         * Entity 后缀
         */
        private String entitySuffix = "Entity";

        /**
         * Mapper 后缀
         */
        private String mapperSuffix = "Mapper";

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
         * Domain 包名
         */
        private String domainSubPackage = "domain";

        /**
         * Entity 包名
         */
        private String entitySubPackage = "entity";

        /**
         * Entity 包名
         */
        private String mapperSubPackage = "mapper";

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

        public String getDomainPackage () {
            return rootPackage + "." + domainSubPackage;
        }

        public String getMapperPackage () {
            return rootPackage + "." + mapperSubPackage;
        }

        public String getSourcesPathAt(String filename) {
            return sourcesPath + "/" + filename;
        }

        public String getJavaPathAt(String filename) {
            return javaPath + "/" + filename;
        }

    }


}
