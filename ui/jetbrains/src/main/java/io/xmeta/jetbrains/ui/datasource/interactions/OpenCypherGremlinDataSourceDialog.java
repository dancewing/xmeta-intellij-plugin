package io.xmeta.jetbrains.ui.datasource.interactions;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.ui.components.JBPasswordField;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.AsyncProcessIcon;
import io.xmeta.impl.OpenCypherGremlinConfiguration;
import io.xmeta.jetbrains.component.datasource.DataSourceType;
import io.xmeta.jetbrains.component.datasource.state.DataSource;
import io.xmeta.jetbrains.configuration.DataSourcesSettings;
import io.xmeta.jetbrains.ui.datasource.DataSourcesView;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

import static io.xmeta.jetbrains.util.Validation.validation;

public class OpenCypherGremlinDataSourceDialog extends DataSourceDialog {

    private static final String COSMOS_DB_USER_REGEX = "^\\/dbs\\/[^\\/\\\\#?]+\\/colls\\/[^\\/\\\\#?]+$";
    private static final String COSMOS_DB_USER_ERROR = "User should be of form '/dbs/__DATABASE_NAME__/colls/__COLLECTION_NAME__' for Cosmos DB";

    private final DataSourcesSettings dataSourcesSettings;
    private DataSource dataSourceToEdit;

    private String dataSourceName;
    private OpenCypherGremlinConfiguration configuration = new OpenCypherGremlinConfiguration();

    private JPanel content;
    private JBTextField dataSourceNameField;
    private JBTextField hostField;
    private JBTextField userField;
    private JBPasswordField passwordField;
    private JBTextField portField;
    private JButton testConnectionButton;
    private JCheckBox useSSLCheckBox;
    private JPanel loadingPanel;
    private AsyncProcessIcon loadingIcon;

    public OpenCypherGremlinDataSourceDialog(Project project, DataSourcesView dataSourcesView, DataSource dataSourceToEdit) {
        this(project, dataSourcesView);
        this.dataSourceToEdit = dataSourceToEdit;
    }

    public OpenCypherGremlinDataSourceDialog(Project project, DataSourcesView dataSourcesView) {
        super(project, dataSourcesView);
        loadingPanel.setVisible(false);
        dataSourcesSettings = DataSourcesSettings.getInstance(project);
        testConnectionButton.addActionListener(e -> this.validationPopup());
        initValidation();
    }

    @NotNull
    @Override
    protected List<ValidationInfo> doValidateAll() {
        List<ValidationInfo> validations = new ArrayList<>();

        if (StringUtils.isBlank(dataSourceNameField.getText())) {
            validations.add(validation("Data source name must not be empty", dataSourceNameField));
        }
        if (StringUtils.isBlank(hostField.getText())) {
            validations.add(validation("Host must not be empty", hostField));
        }
        if (StringUtils.isBlank(portField.getText())) {
            validations.add(validation("Port must not be empty", portField));
        }
        if (!StringUtils.isNumeric(portField.getText())) {
            validations.add(validation("Port must be numeric", portField));
        }

        extractData();

        if (dataSourcesSettings.isDataSourceExists(dataSourceName)) {
            if (!(dataSourceToEdit != null && dataSourceToEdit.getName().equals(dataSourceName))) {
                validations.add(validation(String.format("Data source [%s] already exists", dataSourceName), dataSourceNameField));
            }
        }

        return validations;
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        if (dataSourceToEdit != null) {
            OpenCypherGremlinConfiguration config = new OpenCypherGremlinConfiguration(dataSourceToEdit.getConfiguration());

            dataSourceNameField.setText(dataSourceToEdit.getName());
            hostField.setText(config.getHost());
            portField.setText(config.getPort().toString());
            userField.setText(config.getUser());
            passwordField.setText(config.getPassword());
            useSSLCheckBox.setSelected(config.getSSL());
        }
        return content;
    }

    @Override
    public DataSource constructDataSource() {
        extractData();

        return dataSourcesSettings.createDataSource(
                dataSourceToEdit,
                DataSourceType.OPENCYPHER_GREMLIN,
                dataSourceName,
                configuration.getConfiguration()
        );
    }

    @Override
    protected void showLoading() {
        testConnectionButton.setEnabled(false);
        loadingIcon.resume();
        loadingPanel.setVisible(true);
    }

    @Override
    protected void hideLoading() {
        testConnectionButton.setEnabled(true);
        loadingIcon.suspend();
        loadingPanel.setVisible(false);
    }

    private void extractData() {
        dataSourceName = dataSourceNameField.getText();
        configuration.setHost(hostField.getText());
        configuration.setPort(portField.getText());
        configuration.setUser(userField.getText());
        configuration.setPassword(String.valueOf(passwordField.getPassword()));
        configuration.setSSL(useSSLCheckBox.isSelected());
    }

    private void createUIComponents() {
        loadingIcon = new AsyncProcessIcon("validateConnectionIcon");
    }
}
