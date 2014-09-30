package com.gtnexus.appxpress.pmextractor;

import com.gtnexus.appxpress.pmextractor.exception.PMExtractorException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;

/**
 * Consolidates stored properties file from User, with flags passed from CLI.
 *
 * @author jjdonov
 */
public class ArgsAndPropertiesConsolidator {

    private String[] userArgs;
    private Properties properties;

    /**
     * @param userArgs   Arguments passed from the CLI.
     * @param properties Properties file read from user's AppXpress directory.
     */
    public ArgsAndPropertiesConsolidator(String userArgs[],
                                         Properties properties) {
        this.userArgs = userArgs;
        this.properties = properties;
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
        final Scanner scanner = new Scanner(System.in);
        String input = null;
        int i = 0;
        while (iter.hasNext()) {
            ExtractorOption currentOpt = iter.next();
            String propVal = properties.getProperty(currentOpt.toString());
            if (userArgs.length > i) {
                input = userArgs[i];
            }
            if (input != null && !input.isEmpty()) {
                optMap.put(currentOpt, input);
                properties.put(currentOpt.toString(), input);
            } else if (propVal != null && !propVal.isEmpty()) {
                optMap.put(currentOpt, propVal);
            } else {
                String optValFromUser = getParameterFromUser(currentOpt,
                        scanner);
                optMap.put(currentOpt, optValFromUser);
                properties.put(currentOpt.toString(), optValFromUser);
            }
            i++;
        }
        scanner.close();
        return optMap;
    }

    /**
     * @param option  The ExtractorOption to be query the user for.
     * @param scanner The Scanner used to read in user input.
     * @return The value entered by the user.
     */
    private String getParameterFromUser(ExtractorOption option, Scanner scanner) {
        final Class<?> type = option.getType();
        if (type.equals(Integer.class)) {
            System.out.print("Please enter the number of " + option.toString()
                    + "(s): ");
        } else if (type.equals(String.class)) {
            System.out.print("Do you want " + option.toString() + "? [y/n]: ");
        } else if (type.equals(Boolean.class)) {
            System.out.print("Please enter " + option.toString() + ": ");
        }
        return scanner.next();
    }

    public void presentSaveOption(String propPath) throws PMExtractorException {
        File settingsFile = new File(propPath);
        final Scanner scanner = new Scanner(System.in);
        System.out.print("Save settings? [y/n]: ");
        if ("y".equalsIgnoreCase(scanner.next())) {
            // Save the settings file
            try (FileOutputStream settingsOutputStream = new FileOutputStream(
                    settingsFile)) {
                properties.store(settingsOutputStream, null);
            } catch (IOException e) {
                throw new PMExtractorException("Failed to write properties file!", e);
            }
        }
        scanner.close();
    }

}
