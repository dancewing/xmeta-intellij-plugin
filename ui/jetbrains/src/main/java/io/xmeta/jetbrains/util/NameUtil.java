package io.xmeta.jetbrains.util;

import io.xmeta.jetbrains.component.datasource.state.DataSource;
import io.xmeta.platform.GraphConstants;

public final class NameUtil {

    public static String createDataSourceFileName(DataSource dataSource) {
        return GraphConstants.BOUND_DATA_SOURCE_PREFIX + dataSource.getUUID() + "." + dataSource.getDescription().getDefaultFileExtension();
    }

    public static String extractDataSourceUUID(String fileName) {
        int beginIndex = GraphConstants.BOUND_DATA_SOURCE_PREFIX.length();
        int endIndex = beginIndex + 36;
        return fileName.substring(beginIndex, endIndex);
    }
}
