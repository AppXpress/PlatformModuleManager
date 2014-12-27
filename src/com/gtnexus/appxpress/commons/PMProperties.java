package com.gtnexus.appxpress.commons;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import com.gtnexus.appxpress.AppXpressException;

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

	public String getProperty(String property) {
		return properties.getProperty(property);
	}

	public void put(String key, String value) {
		properties.put(key, value);
	}

	public void store() throws AppXpressException {
		try (FileOutputStream settingsOutputStream = new FileOutputStream(
				propertiesFile)) {
			properties.store(settingsOutputStream, null);
		} catch (IOException e) {
			throw new AppXpressException("Failed to write properties file!",
					e);
		}
	}

}
