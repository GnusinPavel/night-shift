package io.github.gnusinpavel.nightshift.services;

import com.intellij.openapi.components.ServiceManager;

public interface ThemeService {
    static ThemeService getInstance() {
        return ServiceManager.getService(ThemeService.class);
    }

    void ensure();
}
