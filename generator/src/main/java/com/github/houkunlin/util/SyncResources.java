package com.github.houkunlin.util;

import com.github.houkunlin.config.OtherConfig;
import com.intellij.notification.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.util.ExceptionUtil;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 同步插件资源到项目路径
 *
 * @author HouKunLin
 * @date 2020/4/6 0006 19:10
 */
@Data
public class SyncResources implements Runnable {
    private static final String NOTIFICATION_ID = "GraphDatabaseBackgroundSyncNotification";
    /**
     * 插件初始化文件
     */
    private static final String INIT_FILENAME = "init.properties";
    private Project project = PluginUtils.getProject();

    /**
     * 复制插件内部的模板文件到项目路径中
     */
    @Override
    public void run() {
       // checkOldVersion();
        File initFile = PluginUtils.getProjectWorkspacePluginDirFile(INIT_FILENAME);
        boolean initFileExists = initFile.exists();
        if (initFileExists) {
            // 不再强制覆盖 初始化文件
            return;
        }
        try {
            // 只处理不存在的初始化文件
            String content = IO.readResources(INIT_FILENAME);
            FileUtils.copyFile(project, initFile, content, false);
        } catch (Throwable e) {
            ExceptionUtil.rethrow(new RuntimeException("同步插件文件到本地出现错误：\r\n" + e.getMessage(), e));
        }
        try {
            syncFiles();
        } catch (Throwable e) {
            ExceptionUtil.rethrow(new RuntimeException("同步插件文件到本地出现错误：\r\n" + e.getMessage(), e));
        }

        PluginUtils.refreshProject();
    }

    private void checkOldVersion() {

    }

    private void showNotification(Project project, String title,  String message, NotificationType type) {
        NotificationGroup group = new NotificationGroup(NOTIFICATION_ID, NotificationDisplayType.STICKY_BALLOON, true);
        Notification notification = group.createNotification(
                title,
                message,
                type,
                null
        );
        Notifications.Bus.notify(notification, project);
    }
    /**
     * 复制插件内部的代码模板到项目路径中
     *
     * @throws IOException 复制异常
     */
    private void syncFiles() throws IOException {
        String syncFiles = IO.readResources("syncFiles.txt");
        String[] split = syncFiles.split("\n");
        for (String filePath : split) {
            if (StringUtils.isBlank(filePath)) {
                continue;
            }
            InputStream inputStream = IO.getInputStream(filePath);
            if (inputStream == null) {
                continue;
            }
            String content = IO.read(inputStream);

            FileUtils.copyFile(project, PluginUtils.getProjectWorkspacePluginDirFile(filePath), content, true);
        }
    }

    @Nullable
    private DialogWrapper.DoNotAskOption getDoNotAskOption(OtherConfig otherConfig) {
        if (otherConfig == null) {
            return null;
        }
        return new DialogWrapper.DoNotAskOption() {
            @Override
            public boolean isToBeShown() {
                return otherConfig.isShowUpgradeMoveTip();
            }

            @Override
            public void setToBeShown(boolean toBeShown, int exitCode) {
                otherConfig.setShowUpgradeMoveTip(toBeShown);
            }

            @Override
            public boolean canBeHidden() {
                return true;
            }

            @Override
            public boolean shouldSaveOptionsOnCancel() {
                return true;
            }

            @Override
            public @NotNull
            @NlsContexts.Checkbox String getDoNotShowMessage() {
                return "下次不再提示此消息";
            }
        };
    }
}
