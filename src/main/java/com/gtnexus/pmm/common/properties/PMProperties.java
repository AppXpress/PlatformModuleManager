package com.gtnexus.pmm.common.properties;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import com.gtnexus.pmm.AppXpressException;
import com.gtnexus.pmm.api.v100.cli.option.CommandOption;

/**
 * Simple wrapper around a File and Property objects.
 *
 * Created by jjdonov on 9/29/14.
 */
public class PMProperties {

    private File propertiesFile;
    private Properties properties;
    private boolean propertiesHaveChanged;

    public PMProperties(Properties properties, File propertiesFile) {
	this.properties = properties;
	this.propertiesFile = propertiesFile;
	this.propertiesHaveChanged = false;
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

    public String getProperty(String property) {
	return properties.getProperty(property);
    }

    public String getProperty(CommandOption opt) {
	return properties.getProperty(opt.getLongName());
    }

    public void put(String key, String value) {
	if (properties.containsKey(key) && !properties.get(key).equals(value)) {
	    propertiesHaveChanged = true;
	}
	properties.put(key, value);
    }

    public void store() throws AppXpressException {
	try (FileOutputStream settingsOutputStream = new FileOutputStream(propertiesFile)) {
	    properties.store(settingsOutputStream, null);
	} catch (IOException e) {
	    throw new AppXpressException("Failed to write properties file!", e);
	}
    }

    public boolean haveChanged() {
	return propertiesHaveChanged;
    }

}
