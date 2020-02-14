package io.github.gnusinpavel.nightshift.config;

import com.intellij.openapi.options.Configurable;
import io.github.gnusinpavel.nightshift.services.ThemeService;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.text.ParseException;

public class PreferencesConfigurable implements Configurable {
    private PreferencePanel panel;

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "Night Shift";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        try {
            if (panel == null) {
                panel = new PreferencePanel();
            }
            return panel;
        } catch (ParseException e) {
            return null;
        }
    }

    @Override
    public boolean isModified() {
        return panel.isModified();
    }

    @Override
    public void apply() {
        panel.apply();
        ThemeService.getInstance().ensure();

    }

    @Override
    public void disposeUIResources() {
        panel = null;
    }
}
