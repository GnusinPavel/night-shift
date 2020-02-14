package io.github.gnusinpavel.nightshift.services.impl;

import com.intellij.ide.ui.LafManager;
import com.intellij.ide.ui.UISettings;
import com.intellij.ide.ui.laf.IntelliJLookAndFeelInfo;
import com.intellij.ide.ui.laf.darcula.DarculaInstaller;
import com.intellij.ide.ui.laf.darcula.DarculaLookAndFeelInfo;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.colors.impl.AbstractColorsScheme;
import com.intellij.openapi.options.SchemeManager;
import com.intellij.ui.ColorUtil;
import io.github.gnusinpavel.nightshift.config.PluginProperties;
import io.github.gnusinpavel.nightshift.services.ThemeService;

import javax.swing.*;
import java.time.LocalTime;
import java.util.Arrays;

public class ThemeServiceImpl implements ThemeService {
    final EditorColorsManager manager = EditorColorsManager.getInstance();

    @Override
    public void ensure() {
        final LocalTime now = LocalTime.now();
        final PluginProperties properties = new PluginProperties();
        final LocalTime from = properties.fromTime();
        final LocalTime to = properties.toTime();

        if (now.isAfter(from) && now.isBefore(to)) {
            switchTheme(properties.lightTheme());
        } else {
            switchTheme(properties.darkTheme());
        }
    }

    private void switchTheme(String theme) {
        if (!currentTheme().equals(theme)) {
            final EditorColorsScheme scheme = manager.getScheme(theme);
            manager.setGlobalScheme(scheme);
            setLookAndFeelForTheme(scheme);
        }
    }

    private void setLookAndFeelForTheme(EditorColorsScheme theme) {
        final LafManager lafManager = LafManager.getInstance();
        final UIManager.LookAndFeelInfo[] lookAndFeels = lafManager.getInstalledLookAndFeels();
        final boolean dark = ColorUtil.isDark(theme.getDefaultBackground());
        final UIManager.LookAndFeelInfo laf = Arrays.stream(lookAndFeels)
            .filter(l -> SchemeManager.getDisplayName(theme).equals(l.getName()))
            .findFirst()
            .orElseGet(() -> Arrays.stream(lookAndFeels)
                .filter(l -> l.getName().equals(((AbstractColorsScheme) theme).getParentScheme().getName()))
                .findFirst()
                .orElseGet(() -> {
                    if (dark) {
                        return new DarculaLookAndFeelInfo();
                    }
                    return new IntelliJLookAndFeelInfo();
                }));
        final UIManager.LookAndFeelInfo currentLaf = lafManager.getCurrentLookAndFeel();
        if (currentLaf == null || !laf.getName().equals(currentLaf.getName())) {
            lafManager.setCurrentLookAndFeel(laf);
            lafManager.updateUI();
            lafManager.repaintUI();
            SwingUtilities.invokeLater(() -> {
                if (dark) {
                    DarculaInstaller.install();
                } else {
                    DarculaInstaller.uninstall();
                }
            });
            UISettings.getInstance().fireUISettingsChanged();
        }
    }

    private String currentTheme() {
        return SchemeManager.getDisplayName(manager.getGlobalScheme());
    }
}
