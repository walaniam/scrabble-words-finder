package pl.walaniam.srabble;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.walaniam.srabble.gui.FileConfig;
import pl.walaniam.srabble.gui.layout.GlassPane;
import pl.walaniam.srabble.gui.layout.MainFrame;

@Configuration
public class SpringConfig {

    @Bean
    public FileConfig fileConfig() {
        return new FileConfig();
    }

    @Bean
    public GlassPane glassPane() {
        return new GlassPane();
    }

    @Bean
    public MainFrame mainFrame(GlassPane glassPane, FileConfig fileConfig) {
        return new MainFrame(glassPane, fileConfig);
    }
}
