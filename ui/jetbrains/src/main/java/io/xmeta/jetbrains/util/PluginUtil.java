package io.xmeta.jetbrains.util;

import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManager;
import com.intellij.openapi.extensions.PluginId;
import io.xmeta.platform.GraphConstants;

public class PluginUtil {

    private static IdeaPluginDescriptor plugin;

    public static String getVersion() {
        return plugin().getVersion();
    }

    public static boolean isEnabled() {
        return plugin().isEnabled();
    }

    private static IdeaPluginDescriptor plugin() {
        if (plugin == null) {
            plugin = PluginManager.getPlugin(PluginId.getId(GraphConstants.PLUGIN_ID));
        }
        return plugin;
    }
}
