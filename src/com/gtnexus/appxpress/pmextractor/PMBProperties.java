package com.gtnexus.appxpress.pmextractor;

import java.io.File;
import java.util.Properties;

/**
 * Created by jjdonov on 9/29/14.
 */
public class PMBProperties {

    private File propertiesFile;
    private Properties properties;

    public PMBProperties(Properties properties, File propertiesFile) {
        this.properties = properties;
        this.propertiesFile = propertiesFile;
    }

    public File getPropertiesFile() {
        return propertiesFile;
    }

    public String getPropertiesPath() {
        return propertiesFile.getAbsolutePath();
    }

    public void setPropertiesFile(File propertiesFile) {
        this.propertiesFile = propertiesFile;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

}
