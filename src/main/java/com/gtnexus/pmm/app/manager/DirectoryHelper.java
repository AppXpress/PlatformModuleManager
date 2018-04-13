package com.gtnexus.pmm.app.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import com.gtnexus.pmm.api.v100.command.SubCommandException;
import com.gtnexus.pmm.app.AppXpressConstants;
import com.gtnexus.pmm.app.common.properties.PMProperties;

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
     * @throws SubCommandException
     */
    public void ensureAppXpress() throws SubCommandException {
	ensureAppXPressDirectoryExists();
	pmbProperties = getAndEnsurePropertiesExist();
    }

    /**
     * @throws SubCommandException
     *             if the AppXpress directory does not exist and cannot be created.
     */
    private void ensureAppXPressDirectoryExists() throws SubCommandException {
	File appXpressDirectory = resolver.resolveAppXpressDir().toFile();
	if (!appXpressDirectory.isDirectory() && !appXpressDirectory.mkdirs()) {
	    throw new SubCommandException("WARNING: AppXpress directory could not be found"
		    + "and could not be created: " + appXpressDirectory.getAbsolutePath());
	}
    }

    /**
     * @return the properties file if one exists, else a newly created one.
     * @throws SubCommandException
     *             if the properties file does not exist and a new one cannot be
     *             created.
     */
    private PMProperties getAndEnsurePropertiesExist() throws SubCommandException {
	File propertiesFile = new File(getPropertiesFilePath());
	if (!propertiesFile.exists()) {
	    return makePropertiesFile(propertiesFile);
	} else {
	    return readProperties(propertiesFile);
	}
    }

    private PMProperties makePropertiesFile(File propertiesFile) throws SubCommandException {
	try {
	    propertiesFile.createNewFile();
	} catch (IOException e) {
	    throw new SubCommandException("WARNING: Settings file couldn't be created: " + getPropertiesFilePath(), e);
	}
	return new PMProperties(new Properties(), propertiesFile);
    }

    private PMProperties readProperties(File propertiesFile) throws SubCommandException {
	Properties properties = new Properties();
	try (FileInputStream propInputStream = new FileInputStream(propertiesFile)) {
	    properties.load(propInputStream);
	} catch (IOException e) {
	    throw new SubCommandException("Exception while loading properties file.", e);
	}
	return new PMProperties(properties, propertiesFile);
    }

    /**
     *
     * @return the properties file
     * @throws SubCommandException
     */
    public PMProperties getPmProperties() throws SubCommandException {
	if (pmbProperties == null) {
	    throw new SubCommandException("ensureAppXpress() must be called before getPmbProperties");
	}
	return pmbProperties;
    }

}
