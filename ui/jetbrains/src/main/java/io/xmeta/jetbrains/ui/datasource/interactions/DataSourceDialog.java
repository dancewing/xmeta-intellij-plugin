package io.xmeta.jetbrains.ui.datasource.interactions;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.openapi.ui.popup.IconButton;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.util.Disposer;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.ui.JBUI;
import io.xmeta.api.MetaDatabaseApi;
import io.xmeta.jetbrains.component.datasource.state.DataSource;
import io.xmeta.jetbrains.database.DatabaseManagerService;
import io.xmeta.jetbrains.services.ExecutorService;
import io.xmeta.jetbrains.ui.datasource.DataSourcesView;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

import static io.xmeta.impl.exceptions.ExceptionWrapper.getCause;

public abstract class DataSourceDialog extends DialogWrapper {
    public static final int THICKNESS = 10;
    public static final int HEIGHT = 150;

    protected DataSourceDialog(@Nullable Project project, DataSourcesView dataSourcesView) {
        super(project);
        Disposer.register(project, myDisposable);
        init();
    }

    public abstract DataSource constructDataSource();

    protected abstract void showLoading();

    protected abstract void hideLoading();

    public boolean go() {
        init();
        return showAndGet();
    }


    public void validationPopup() {
        JPanel popupPanel = new JPanel(new BorderLayout());
        popupPanel.setBorder(JBUI.Borders.empty(THICKNESS));

        ValidationInfo validationInfo = doValidate();
        if (validationInfo != null) {
            JLabel connectionFailed = new JLabel("Connection failed: " + validationInfo.message,
                    AllIcons.General.Error, JLabel.LEFT);
            popupPanel.add(connectionFailed, BorderLayout.CENTER);
            createPopup(popupPanel, getContentPanel());
        } else {
            validateConnection(popupPanel, getContentPanel());
        }
    }

    private void createPopup(JPanel popupPanel, JComponent contentPanel) {
        if (contentPanel.isShowing()) {
            JBPopupFactory.getInstance()
                    .createComponentPopupBuilder(popupPanel, getPreferredFocusedComponent())
                    .setCancelButton(new IconButton("Close", AllIcons.Actions.Close))
                    .setTitle("Test connection")
                    .setResizable(true)
                    .setMovable(true)
                    .setCancelButton(new IconButton("Close", AllIcons.Actions.Close, AllIcons.Actions.CloseHovered))
                    .createPopup()
                    .showInCenterOf(contentPanel);
        }
    }

    private void validateConnection(
            JPanel popupPanel,
            JComponent contentPanel) {
        ExecutorService executorService = ServiceManager.getService(ExecutorService.class);
        showLoading();
        executorService.runInBackground(
                this::executeOkQuery,
                (status) -> connectionSuccessful(popupPanel, contentPanel),
                (exception) -> connectionFailed(exception, popupPanel, contentPanel),
                ModalityState.current()
        );
    }

    private String executeOkQuery() {
        DataSource dataSource = constructDataSource();
        DatabaseManagerService databaseManager = ServiceManager.getService(DatabaseManagerService.class);
        MetaDatabaseApi db = databaseManager.getDatabaseFor(dataSource);

        try {
            String value = db.getToken();
            if (StringUtils.isNotEmpty(value)) {
                return "ok";
            } else {
                return "Test connection failed";
            }
        } catch (Exception e) {
            throw new RuntimeException("Unexpected test query output: " + e.getMessage());
        }
    }

    private void connectionSuccessful(
            JPanel popupPanel,
            JComponent contentPanel) {
        hideLoading();
        JLabel connectionSuccessful = new JLabel("Connection successful!", AllIcons.General.InspectionsOK, JLabel.LEFT);
        popupPanel.add(connectionSuccessful, BorderLayout.CENTER);

        createPopup(popupPanel, contentPanel);
    }

    private void connectionFailed(
            Exception exception,
            JPanel popupPanel,
            JComponent contentPanel) {
        hideLoading();

        JLabel connectionFailed = new JLabel("Connection failed: " +
                exception.getMessage(), AllIcons.General.Error, JLabel.LEFT);

        JTextArea exceptionCauses = new JTextArea();
        exceptionCauses.setLineWrap(false);
        exceptionCauses.append(getCause(exception));

        JBScrollPane scrollPane = new JBScrollPane(exceptionCauses);
        scrollPane.setPreferredSize(new Dimension(-1, HEIGHT));
        popupPanel.add(connectionFailed, BorderLayout.NORTH);
        popupPanel.add(scrollPane, BorderLayout.CENTER);

        createPopup(popupPanel, contentPanel);
    }
}
