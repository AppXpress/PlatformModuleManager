package com.gtnexus.appxpress;

import java.io.File;
import java.util.Properties;

/**
 * Simple wrapper around a File and Property objects.
 *
 * Created by jjdonov on 9/29/14.
 */
public class PMProperties {

    private File propertiesFile;
    private Properties properties;

    public PMProperties(Properties properties, File propertiesFile) {
        this.properties = properties;
        this.propertiesFile = propertiesFile;
    }

    public File getPropertiesFile() {
        return propertiesFile;
    }
    public String getPropertiesPath() {
        return propertiesFile.getAbsolutePath();
    }
    public Properties getProperties() {
        return properties;
    }

}
