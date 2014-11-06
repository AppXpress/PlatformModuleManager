package com.gtnexus.appxpress.pmextractor;

import static com.gtnexus.appxpress.AppXpressConstants.APPXRESS_FOLDER;
import static com.gtnexus.appxpress.AppXpressConstants.SETTINGS_FILENAME;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import com.gtnexus.appxpress.PMBProperties;
import com.gtnexus.appxpress.pmextractor.exception.PMExtractorException;

/**
 * Helper class used by PlatformModuleExtractor. Ensures the existence of necessary directories and files.
 * If they do not exist, DirectoryHelper tries to create them.
 *
 * @author jjdonov
 */
public class DirectoryHelper {

    private static final String USER_HOME = "user.home";
    private static final String dirTemplate = "%s" + File.separator + APPXRESS_FOLDER + File.separator + "%s";
    private String homeDir;
    private String propertiesFilePath;
    private PMBProperties pmbProperties;

    public DirectoryHelper() {
        this.homeDir = System.getProperty(USER_HOME);
        this.propertiesFilePath = String.format(dirTemplate, homeDir, SETTINGS_FILENAME);
    }

    private String getPropertiesFilePath() {
       return propertiesFilePath;
    }

    /**
     * Ensures AppXPress Directories and associated files exist on the system.
     *
     * @throws PMExtractorException
     */
    public void ensureAppXpress() throws PMExtractorException {
        ensureAppXPressDirectoryExists();
        pmbProperties = getAndEnsurePropertiesExist();
    }

    /**
     * @throws PMExtractorException if the AppXpress directory does not exist
     *                              and cannot be created.
     */
    private void ensureAppXPressDirectoryExists() throws PMExtractorException {
        File appXpressDirectory = new File(homeDir + File.separator
                + APPXRESS_FOLDER);
        if (!appXpressDirectory.isDirectory() && !appXpressDirectory.mkdirs()) {
            throw new PMExtractorException("WARNING: AppXpress directory could not be found"
                    + "and could not be created: "
                    + appXpressDirectory.getAbsolutePath());
        }
    }

    /**
     * @return the properties file if one exists, else a newly created one.
     * @throws PMExtractorException if the properties file does not exist and a new one cannot be created.
     */
    private PMBProperties getAndEnsurePropertiesExist() throws PMExtractorException {
        File propertiesFile = new File(getPropertiesFilePath());
        if (!propertiesFile.exists()) {
            return makePropertiesFile(propertiesFile);
        } else {
            return readProperties(propertiesFile);
        }
    }

    private PMBProperties makePropertiesFile(File propertiesFile) throws PMExtractorException {
        try {
            propertiesFile.createNewFile();
        } catch (IOException e) {
            throw new PMExtractorException("WARNING: Settings file couldn't be created: " + getPropertiesFilePath(), e);
        }
        return new PMBProperties(new Properties(), propertiesFile);
    }

    private PMBProperties readProperties(File propertiesFile) throws PMExtractorException {
        Properties properties = new Properties();
        try (FileInputStream propInputStream = new FileInputStream(propertiesFile)) {
            properties.load(propInputStream);
        } catch (IOException e) {
            throw new PMExtractorException("Exception while loading properties file.", e);
        }
        return new PMBProperties(properties, propertiesFile);
    }


    /**
     *
     * @return the properties file
     * @throws PMExtractorException
     */
    public PMBProperties getPmbProperties() throws PMExtractorException {
        if (pmbProperties == null) {
            throw new PMExtractorException("ensureAppXpress() must be called before getPmbProperties");
        }
        return pmbProperties;
    }
}
