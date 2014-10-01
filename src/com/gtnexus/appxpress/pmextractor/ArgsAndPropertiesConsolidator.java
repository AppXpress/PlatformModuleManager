package com.gtnexus.appxpress.pmextractor;

import com.gtnexus.appxpress.Asker;
import com.gtnexus.appxpress.pmextractor.exception.PMExtractorException;

import java.io.*;
import java.util.*;

/**
 * Consolidates stored properties file from User, with flags passed from CLI.
 *
 * @author jjdonov
 */
public class ArgsAndPropertiesConsolidator {

    private String[] userArgs;
    private Properties properties;
    private Asker asker;

    /**
     * @param userArgs   Arguments passed from the CLI.
     * @param properties Properties file read from user's AppXpress directory.
     */
    public ArgsAndPropertiesConsolidator(String userArgs[],
                                         Properties properties) {
        this.userArgs = userArgs;
        this.properties = properties;
        this.asker = new Asker(System.in, System.out);
    }

    /**
     * @param userArgs    Arguments passed from the CLI.
     * @param properties  Properties file read from user's AppXpress directory.
     * @param inputStream The input stream that this consolidator should read from.
     * @param printStream The printStream that this consolidator should write to.
     */
    public ArgsAndPropertiesConsolidator(String userArgs[],
                                         Properties properties,
                                         InputStream inputStream,
                                         PrintStream printStream) {
        this.userArgs = userArgs;
        this.properties = properties;
        this.asker = new Asker(inputStream, printStream);
    }


    /**
     * @return the map of consolidated ExtractorOptions and their values.
     */
    public Map<ExtractorOption, String> consolidate() {
        final Set<ExtractorOption> options = EnumSet
                .allOf(ExtractorOption.class);
        final Map<ExtractorOption, String> optMap = new EnumMap<>(
                ExtractorOption.class);
        final Iterator<ExtractorOption> iter = options.iterator();
        String input = null;
        int i = 0;
        while (iter.hasNext()) {
            ExtractorOption currentOpt = iter.next();
            String val = consolidateSingle(currentOpt, i);
            properties.put(currentOpt.toString(), val);
            optMap.put(currentOpt, val);
            i++;
        }
        return optMap;
    }

    private String consolidateSingle(ExtractorOption option, int index) {
        String input = null;
        String propVal = properties.getProperty(option.toString());
        if (userArgs.length > index) {
            input = userArgs[index];
        }
        if (input != null && !input.isEmpty()) {
            return input;
        } else if (propVal != null && !propVal.isEmpty()) {
            return propVal;
        } else {
            return getParameterFromUser(option);
        }
    }

    /**
     * @param option The ExtractorOption to be query the user for.
     * @return The value entered by the user.
     */
    private String getParameterFromUser(ExtractorOption option) {
        String val = asker.ask(option.getMessage());
        while (!option.isValid(val)) {
            val = asker.ask("Invalid input. Please try again.");
        }
        return val;
    }

    public void presentSaveOption(String propPath) throws PMExtractorException {
        File settingsFile = new File(propPath);
        String answer = asker.ask("Save settings? [y/n]: ");
        while (!answer.equalsIgnoreCase("Y") && !answer.equalsIgnoreCase("N")) {
            answer = asker.ask("Ivalid input. Please try again.");
        }
        if (answer.equalsIgnoreCase("Y")) {
            // Save the settings file
            try (FileOutputStream settingsOutputStream = new FileOutputStream(
                    settingsFile)) {
                properties.store(settingsOutputStream, null);
            } catch (IOException e) {
                throw new PMExtractorException("Failed to write properties file!", e);
            }
        }
    }
}
