package com.gtnexus.appxpress;

import java.io.File;

public interface AppXpressConstants {

	public static final String APPXRESS_FOLDER = ".appxpress";
	public static final String SETTINGS_FILENAME = "settings.properties";
	public static final String PLATFORM_MODULE_UNZIP_NAME = "PlatModX";
	public static String FS = File.separator; // Not static cause it can change
												// based on OS
}
