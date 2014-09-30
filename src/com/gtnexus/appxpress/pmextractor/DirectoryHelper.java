package com.gtnexus.appxpress.pmextractor;

import com.gtnexus.appxpress.pmextractor.exception.PMExtractorException;

import static com.gtnexus.appxpress.AppXpressConstants.APPXRESS_FOLDER;
import static com.gtnexus.appxpress.AppXpressConstants.SETTINGS_FILENAME;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Helper class used by PlatformModuleExtractor. Ensures the existence of necessary directories and files.
 * If they do not exist, DirectoryHelper tries to create them.
 *
 * @author jjdonov
 */
public class DirectoryHelper {

    public static final String USER_HOME = "user.home";
    private static final String dirTemplate = "%s" + File.separator + APPXRESS_FOLDER + File.separator + "%s";
    private String homeDir;
    private String propertiesFilePath;
    private PMBProperties pmbProperties;

    public DirectoryHelper() {
        homeDir = System.getProperty(USER_HOME);
    }

    private String getPropertiesFilePath() {
        if (propertiesFilePath == null) {
            return propertiesFilePath = String.format(dirTemplate, homeDir, SETTINGS_FILENAME);
        } else {
            return propertiesFilePath;
        }
    }

    public void ensureAppXpress() throws PMExtractorException {
        ensureAppXPressDirectoryExists();
        pmbProperties = getAndEnsurePropertiesExist();
    }

    private File ensureAppXPressDirectoryExists() throws PMExtractorException {
        File appXpressDirectory = new File(homeDir + File.separator
                + APPXRESS_FOLDER);
        if (!appXpressDirectory.isDirectory() && !appXpressDirectory.mkdirs()) {
            throw new PMExtractorException("WARNING: AppXpress directory could not be found"
                    + "and could not be created: "
                    + appXpressDirectory.getAbsolutePath());
        }
        return appXpressDirectory;
    }

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
        Properties properties = null;
        try (FileInputStream propInputStream = new FileInputStream(propertiesFile)) {
            properties.load(propInputStream);
            propInputStream.close();
        } catch (IOException e) {
            throw new PMExtractorException("Exception while loading properties file.", e);
        }
        return new PMBProperties(properties, propertiesFile);
    }


    public PMBProperties getPmbProperties() throws PMExtractorException{
        if(pmbProperties == null) {
            throw new PMExtractorException("ensureAppXpress() must be called before getPmbProperties");
        }
        return pmbProperties;
    }
}
