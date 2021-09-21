package io.xmeta.jetbrains.configuration;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.ui.components.JBCheckBox;
import io.xmeta.jetbrains.component.settings.SettingsComponent;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class GraphDatabaseSupportConfiguration implements Configurable {

    private boolean isModified = false;
    private JBCheckBox analyticsCheckBox, globalParametersCheckbox;
    private JBCheckBox invertZoomCheckBox;

    @Override
    public String getDisplayName() {
        return "XMeta";
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return "XMeta support plugin configuration";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        isModified = false;

        analyticsCheckBox = new JBCheckBox("Send anonymous usage statistics",
                SettingsComponent.getInstance().isAnalyticEnabled());
        analyticsCheckBox.addActionListener(e -> isModified = true);

        invertZoomCheckBox = new JBCheckBox("Invert mouse wheel zoom controls in Graph View",
                SettingsComponent.getInstance().isGraphViewZoomInverted());
        invertZoomCheckBox.addActionListener(e -> isModified = true);

        JPanel rootPane = new JPanel();
        rootPane.setLayout(new BoxLayout(rootPane, BoxLayout.Y_AXIS));
        rootPane.add(analyticsCheckBox);
        rootPane.add(invertZoomCheckBox);

        return rootPane;
    }

    @Override
    public boolean isModified() {
        return isModified;
    }

    @Override
    public void apply() throws ConfigurationException {
        SettingsComponent instance = SettingsComponent.getInstance();
        instance.enableAnalytics(analyticsCheckBox.isSelected());
        instance.invertGraphViewZoom(invertZoomCheckBox.isSelected());

        //ApplicationManager.getApplication().getMessageBus().syncPublisher(PluginSettingsUpdated.TOPIC)
        // .settingsUpdated();

        isModified = false;
    }

    @Override
    public void reset() {
        analyticsCheckBox.setSelected(SettingsComponent.getInstance().isAnalyticEnabled());
        invertZoomCheckBox.setSelected(SettingsComponent.getInstance().isGraphViewZoomInverted());
        isModified = false;
    }

    @Override
    public void disposeUIResources() {
    }
}
