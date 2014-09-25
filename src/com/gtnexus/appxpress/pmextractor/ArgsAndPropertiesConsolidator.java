package com.gtnexus.appxpress.pmextractor;

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

public class ArgsAndPropertiesConsolidator {

	private String[] userArgs;
	private Properties properties;

	public ArgsAndPropertiesConsolidator(String userArgs[],
			Properties properties) {
		this.userArgs = userArgs;
		this.properties = properties;
	}

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

	private String getParameterFromUser(ExtractorOption option, Scanner scanner) {
		Class<?> type = option.getType();
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

	public void presentSaveOption(String propPath) {
		File settingsFile = new File(propPath);
		final Scanner scanner = new Scanner(System.in);
		System.out.print("Save settings? [y/n]: ");
		if ("y".equalsIgnoreCase(scanner.next())) {
			// Save the settings file
			try (FileOutputStream settingsOutputStream = new FileOutputStream(
					settingsFile)) {
				properties.store(settingsOutputStream, null);
			} catch (IOException e) {
				System.err.println("Failed to write properties!");
				e.printStackTrace();
			}
		}
		scanner.close();
	}

}
