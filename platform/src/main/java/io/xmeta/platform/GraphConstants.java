package io.xmeta.platform;

public final class GraphConstants {

    public static final boolean IS_DEVELOPMENT = System.getProperty("graphDatabaseSupportDevelopment") != null;

    public static final String BOUND_DATA_SOURCE_PREFIX = "graphdbBoundDataSource-";
    public static final String PLUGIN_ID = "io.xmeta.intellij.plugin";

    private GraphConstants() {
    }
}
