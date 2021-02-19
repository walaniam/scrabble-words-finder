package pl.walaniam.srabble.gui;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.PreDestroy;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

@Slf4j
public class FileConfig {
    
    public static final int FONT_SIZE_INCREMENT = 2;
    public static final String DICTIONARY_FILE_PATH = "dictionary.file.path";

    private Properties properties;
    private boolean dirty;

    private void load() {

        if (properties == null) {
            
            File configFile = getConfigurationFile();
            properties = new Properties();
            dirty = false;
            
            if (configFile != null && configFile.exists()) {
                
                log.info("Loading configuration from file: {}", configFile.getAbsolutePath());

                try (FileInputStream configStream = new FileInputStream(configFile)) {
                    properties.load(configStream);
                } catch (IOException e) {
                    log.error("Cannot read configuration file from path: " + configFile.getAbsolutePath(), e);
                }
            }
        }
    }

    @PreDestroy
    public void save() {
        
        if (properties == null || !dirty) {
            return;
        }

        File configFile = getConfigurationFile();
        if (configFile != null) {
            
            log.info("Saving configuration to file: {}", configFile.getAbsolutePath());

            if (configFile.exists()) {
                configFile.delete();                
            }

            try (FileOutputStream fileOut = new FileOutputStream(configFile)) {
                properties.store(fileOut, "");
                fileOut.flush();
                dirty = false;
            } catch (IOException e) {
                log.error("Cannot write configuration to path " + configFile.getAbsolutePath(), e);
            }
        }
    }
    
    public String getProperty(String key) {
        if (properties == null) {
            load();
        }
        return properties.getProperty(key);
    }
    
    public void setProperty(String key, String value) {
        if (properties == null) {
            load();
        }
        properties.setProperty(key, value);
        dirty = true;
    }
    
    private static File getConfigurationFile() {
        File file = null;
        String userHome = System.getProperty("user.home");
        if (userHome != null) {
            File homeDir = new File(userHome);
            file = new File(homeDir, "pl-walaniam-scrabble-config.properties");
        }
        return file;
    }

}
