package io.github.gnusinpavel.nightshift.config;

import com.intellij.ide.util.PropertiesComponent;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class PluginProperties {
    private final static String PREFIX = "NightShift_";

    private final PropertiesComponent propertiesComponent = PropertiesComponent.getInstance();

    public String lightTheme() {
        return propertiesComponent.getValue(withPrefix("lightTheme"), "Default");
    }

    void lightTheme(String theme) {
        propertiesComponent.setValue(withPrefix("lightTheme"), theme);
    }

    public String darkTheme() {
        return propertiesComponent.getValue(withPrefix("darkTheme"), "Darcula");
    }

    void darkTheme(String theme) {
        propertiesComponent.setValue(withPrefix("darkTheme"), theme);
    }

    public LocalTime fromTime() {
        final String fromTime = propertiesComponent.getValue(withPrefix("fromTime"), "18:00");
        return LocalTime.parse(fromTime, DateTimeFormatter.ofPattern("HH:mm"));
    }

    void fromTime(LocalTime time) {
        propertiesComponent.setValue(withPrefix("fromTime"), time.format(DateTimeFormatter.ofPattern("HH:mm")));
    }

    public LocalTime toTime() {
        final String toTime = propertiesComponent.getValue("toTime", "08:00");
        return LocalTime.parse(toTime, DateTimeFormatter.ofPattern("HH:mm"));
    }

    void toTime(LocalTime time) {
        propertiesComponent.setValue("toTime", time.format(DateTimeFormatter.ofPattern("HH:mm")));
    }

    private String withPrefix(String key) {
        return PREFIX + key;
    }
}
