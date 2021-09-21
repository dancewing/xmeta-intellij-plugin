package io.xmeta.jetbrains.component.startup;

import io.xmeta.generator.util.PluginUtils;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import org.jetbrains.annotations.NotNull;

public class PluginStartupActivity implements StartupActivity, DumbAware {
    @Override
    public void runActivity(@NotNull Project project) {
        PluginUtils.setProject(project);
        PluginUtils.syncResources();
    }
}
