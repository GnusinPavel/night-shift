package io.github.gnusinpavel.nightshift;

import com.intellij.openapi.project.Project;
import io.github.gnusinpavel.nightshift.services.ThemeService;
import org.jetbrains.annotations.NotNull;

public class StartupActivity implements com.intellij.openapi.startup.StartupActivity {

    @Override
    public void runActivity(@NotNull Project project) {
        ThemeService.getInstance().ensure();
    }
}
