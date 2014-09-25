package com.gtnexus.appxpress.pmextractor;

import static com.gtnexus.appxpress.AppXpressConstants.APPXRESS_FOLDER;
import static com.gtnexus.appxpress.AppXpressConstants.SETTINGS_FILENAME;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DirectoryHelper {

	private String homeDir;
	private String propertiesFilePath;

	public DirectoryHelper() {
		homeDir = System.getProperty("user.home");
	}

	public String getPropertiesFilePath() {
		if(propertiesFilePath == null) {
			return homeDir + File.separator + APPXRESS_FOLDER
					+ File.separator + SETTINGS_FILENAME;
		} else return propertiesFilePath;
	}

	public Properties getAndEnsurePropertiesExist() throws IOException {
		File propertiesFile = new File(getPropertiesFilePath());
		if (!propertiesFile.exists() && !propertiesFile.createNewFile()) {
			System.out.println("WARNING: Settings file couldn't be created: "
					+ homeDir + File.separator + APPXRESS_FOLDER
					+ File.separator + SETTINGS_FILENAME);
		}
		FileInputStream propInputStream = new FileInputStream(propertiesFile);
		Properties properties = new Properties();
		properties.load(propInputStream);
		propInputStream.close();
		return properties;
	}

	// TODO
	public  File getAndEnsureAppXPressDirectoryExists() {
		File appXpressDirectory = new File(homeDir + File.separator
				+ APPXRESS_FOLDER);
		if (!appXpressDirectory.isDirectory() && !appXpressDirectory.mkdirs()) {
			System.out
			.println("WARNING: AppXpress directory could not be found"
					+ "and could not be created: "
					+ appXpressDirectory.getAbsolutePath());
		}
		return appXpressDirectory;
	}

}
