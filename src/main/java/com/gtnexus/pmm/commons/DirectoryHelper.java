package com.gtnexus.pmm.commons;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import com.gtnexus.pmm.AppXpressConstants;
import com.gtnexus.pmm.AppXpressDirResolver;
import com.gtnexus.pmm.AppXpressException;
import com.gtnexus.pmm.commons.properties.PMProperties;

/**
 * Helper class used by pmm. Ensures the existence of necessary directories and
 * files. If they do not exist, DirectoryHelper tries to create them.
 *
 * @author jjdonov
 */
public class DirectoryHelper {

    private String propertiesFilePath;
    private PMProperties pmbProperties;
    private final AppXpressDirResolver resolver;

    public DirectoryHelper() {
	this.resolver = new AppXpressDirResolver();
	this.propertiesFilePath = resolver.resolveAppXpressDir().resolve(AppXpressConstants.PROPERTIES_FILE_NAME)
		.toString();
    }

    @Deprecated
    public DirectoryHelper(final String settingsFileName) {
	this.resolver = new AppXpressDirResolver();
	this.propertiesFilePath = resolver.resolveAppXpressDir().resolve(settingsFileName).toString();
    }

    private String getPropertiesFilePath() {
	return propertiesFilePath;
    }

    /**
     * Ensures AppXPress Directories and associated files exist on the system.
     *
     * @throws AppXpressException
     */
    public void ensureAppXpress() throws AppXpressException {
	ensureAppXPressDirectoryExists();
	pmbProperties = getAndEnsurePropertiesExist();
    }

    /**
     * @throws AppXpressException
     *             if the AppXpress directory does not exist and cannot be created.
     */
    private void ensureAppXPressDirectoryExists() throws AppXpressException {
	File appXpressDirectory = resolver.resolveAppXpressDir().toFile();
	if (!appXpressDirectory.isDirectory() && !appXpressDirectory.mkdirs()) {
	    throw new AppXpressException("WARNING: AppXpress directory could not be found"
		    + "and could not be created: " + appXpressDirectory.getAbsolutePath());
	}
    }

    /**
     * @return the properties file if one exists, else a newly created one.
     * @throws AppXpressException
     *             if the properties file does not exist and a new one cannot be
     *             created.
     */
    private PMProperties getAndEnsurePropertiesExist() throws AppXpressException {
	File propertiesFile = new File(getPropertiesFilePath());
	if (!propertiesFile.exists()) {
	    return makePropertiesFile(propertiesFile);
	} else {
	    return readProperties(propertiesFile);
	}
    }

    private PMProperties makePropertiesFile(File propertiesFile) throws AppXpressException {
	try {
	    propertiesFile.createNewFile();
	} catch (IOException e) {
	    throw new AppXpressException("WARNING: Settings file couldn't be created: " + getPropertiesFilePath(), e);
	}
	return new PMProperties(new Properties(), propertiesFile);
    }

    private PMProperties readProperties(File propertiesFile) throws AppXpressException {
	Properties properties = new Properties();
	try (FileInputStream propInputStream = new FileInputStream(propertiesFile)) {
	    properties.load(propInputStream);
	} catch (IOException e) {
	    throw new AppXpressException("Exception while loading properties file.", e);
	}
	return new PMProperties(properties, propertiesFile);
    }

    /**
     *
     * @return the properties file
     * @throws AppXpressException
     */
    public PMProperties getPmProperties() throws AppXpressException {
	if (pmbProperties == null) {
	    throw new AppXpressException("ensureAppXpress() must be called before getPmbProperties");
	}
	return pmbProperties;
    }

}
