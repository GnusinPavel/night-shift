package io.github.gnusinpavel.nightshift.config;

import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.options.SchemeManager;
import com.intellij.ui.ColorUtil;

import javax.swing.*;
import javax.swing.text.DateFormatter;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class PreferencePanel extends JPanel {
    private final PluginProperties properties = new PluginProperties();
    private final EditorColorsManager manager = EditorColorsManager.getInstance();

    private JPanel root;
    private JComboBox<String> lightThemeCb;
    private JComboBox<String> darkThemeCb;
    private JFormattedTextField fromTimeTextField;
    private JFormattedTextField toTimeTextField;

    PreferencePanel() throws ParseException {
        fromTimeTextField.setFormatterFactory(
            new DefaultFormatterFactory(
                new DateFormatter(new SimpleDateFormat("HH:mm")),
                new MaskFormatter("##:##"),
                new MaskFormatter("##:##")
            )
        );
        toTimeTextField.setFormatterFactory(
            new DefaultFormatterFactory(
                new DateFormatter(new SimpleDateFormat("HH:mm")),
                new MaskFormatter("##:##"),
                new MaskFormatter("##:##")
            )
        );
        setLayout(new BorderLayout());
        add(root);
        setVisible(true);

        init();
    }

    private void init() {
        Arrays.stream(manager.getAllSchemes())
            .filter(s -> ColorUtil.isDark(s.getDefaultBackground()))
            .map(SchemeManager::getDisplayName)
            .forEach(s -> darkThemeCb.addItem(s));
        Arrays.stream(manager.getAllSchemes())
            .filter(s -> !ColorUtil.isDark(s.getDefaultBackground()))
            .map(SchemeManager::getDisplayName)
            .forEach(s -> lightThemeCb.addItem(s));

        darkThemeCb.setSelectedItem(properties.darkTheme());
        lightThemeCb.setSelectedItem(properties.lightTheme());
        fromTimeTextField.setValue(properties.fromTime());
        toTimeTextField.setValue(properties.toTime());
    }


    boolean isModified() {
        if (!properties.darkTheme().equals(darkThemeCb.getItemAt(darkThemeCb.getSelectedIndex()))) {
            return true;
        } else if (!properties.lightTheme().equals(lightThemeCb.getItemAt(lightThemeCb.getSelectedIndex()))) {
            return true;
        } else if (!properties.fromTime().equals(parseTime(fromTimeTextField.getText()))) {
            return true;
        } else {
            return !properties.toTime().equals(parseTime(toTimeTextField.getText()));
        }
    }

    void apply() {
        properties.darkTheme(darkThemeCb.getItemAt(darkThemeCb.getSelectedIndex()));
        properties.lightTheme(lightThemeCb.getItemAt(lightThemeCb.getSelectedIndex()));
        properties.fromTime(parseTime(fromTimeTextField.getText()));
        properties.toTime(parseTime(toTimeTextField.getText()));
    }

    private LocalTime parseTime(String value) {
        return LocalTime.parse(value, DateTimeFormatter.ofPattern("HH:mm"));
    }
}
