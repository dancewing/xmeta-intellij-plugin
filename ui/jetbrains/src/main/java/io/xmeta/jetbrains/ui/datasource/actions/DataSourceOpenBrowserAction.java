package io.xmeta.jetbrains.ui.datasource.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import io.xmeta.jetbrains.component.datasource.state.DataSource;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class DataSourceOpenBrowserAction extends AnAction {

    private DataSource dataSource;

    DataSourceOpenBrowserAction(String title, String description, Icon icon, DataSource dataSource) {
        super(title, description, icon);
        this.dataSource = dataSource;
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        try {
            String host = dataSource.getConfiguration().get("host");
            openWebpage(new URI("http://" + host + ":7474"));
        } catch (IOException | URISyntaxException ex) {
            ex.printStackTrace();
        }
    }

    private void openWebpage(URI uri) throws IOException {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            desktop.browse(uri);
        }
    }

}
