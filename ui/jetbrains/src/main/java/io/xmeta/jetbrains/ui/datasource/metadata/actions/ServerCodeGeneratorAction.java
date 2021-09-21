package io.xmeta.jetbrains.ui.datasource.metadata.actions;

import io.xmeta.api.data.MetaEntity;
import io.xmeta.generator.config.ConfigService;
import io.xmeta.generator.task.GeneratorTask;
import io.xmeta.generator.ui.win.server.MainDialog;
import io.xmeta.generator.util.PluginUtils;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.NlsActions;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.List;

/**
 * 操作入口
 *
 * @author HouKunLin
 * @date 2020/4/6 0006 18:08
 */
public class ServerCodeGeneratorAction extends AnAction {

    private final List<MetaEntity> entities;

    public ServerCodeGeneratorAction(@Nullable List<MetaEntity> entities, @Nullable @NlsActions.ActionText String text,
                                     @Nullable @NlsActions.ActionDescription String description, @Nullable Icon icon) {
        super(text, description, icon);
        this.entities = entities;
    }

    /**
     * 在 Database 面板中右键打开插件主页面
     *
     * @param actionEvent 操作对象
     */
    @Override
    public void actionPerformed(AnActionEvent actionEvent) {
        if (entities == null || entities.size() == 0) {
            Messages.showWarningDialog("请至少选择一张表", "通知");
            return;
        }

        Project project = actionEvent.getData(PlatformDataKeys.PROJECT);
        if (project == null) {
            Messages.showErrorDialog("无法获取到当前项目的 Project 对象", "错误");
            return;
        }
        PluginUtils.setProject(project);
        PluginUtils.syncResources();
        ConfigService configService = ConfigService.getInstance(project);
        if (configService == null) {
            Messages.showWarningDialog("初始化配置信息失败，但并不影响继续使用！", "错误");
            configService = new ConfigService();
        }
        MainDialog dialog = new MainDialog(project, entities.toArray(new MetaEntity[]{}), configService);
        if (dialog.go()) {
            GeneratorTask task = dialog.getTask();
            if (task != null) {
                ProgressManager.getInstance().run(task);
            }
        }
    }
}
