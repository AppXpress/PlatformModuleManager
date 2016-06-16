package com.gtnexus.appxpress.cli;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.gtnexus.appxpress.cli.asker.Asker;
import com.gtnexus.appxpress.cli.asker.SimpleAsker;
import com.gtnexus.appxpress.cli.option.AppXpressOption;
import com.gtnexus.appxpress.commons.PMProperties;

/**
 * Consolidates stored properties file from User, with flags passed from CLI.
 * <p>
 * Flags passed from the CLI take precedence over what is stored in the
 * properties file. If neither is present for a mandatory ExtractorOption, then
 * the user is prompted for input.
 *
 * @author jjdonov
 * @param <M>
 */
public class CLIOptsAndPropConsolidator<M extends AppXpressOption> {

	private final Map<M, String> userArgs;
	private final Set<AppXpressOption> optSet;
	private final PMProperties properties;
	private final SimpleAsker asker;

	/**
	 * 
	 * @param userArgs
	 *            Arguments passed from the CLI.
	 * @param optSet
	 * @param properties
	 *            Properties file read from user's AppXpress directory.
	 */
	public CLIOptsAndPropConsolidator(Map<M, String> userArgs,
			Set<AppXpressOption> optSet, PMProperties properties) {
		this.userArgs = userArgs;
		this.optSet = optSet;
		this.properties = properties;
		this.asker = new SimpleAsker(System.in, System.out);
	}

	/**
	 * @param userArgs
	 *            Arguments passed from the CLI.
	 * @param properties
	 *            Properties file read from user's AppXpress directory.
	 * @param inputStream
	 *            The input stream that this consolidator should read from.
	 * @param printStream
	 *            The printStream that this consolidator should write to.
	 */
	public CLIOptsAndPropConsolidator(Map<M , String> userArgs,
			Set<AppXpressOption> optSet, PMProperties properties, InputStream inputStream,
			PrintStream printStream) {
		this.userArgs = userArgs;
		this.optSet = optSet;
		this.properties = properties;
		this.asker = new SimpleAsker(inputStream, printStream);
	}

	/**
	 * @return the map of consolidated ExtractorOptions and their values.
	 */
	public Map<AppXpressOption, String> consolidate() {
		final Map<AppXpressOption, String> optMap = new HashMap<>();
		for (AppXpressOption opt : optSet) {
			if(!opt.shouldBeOmitted()) { //TODO: what is this ommitted logic? seems like it is better suited somewhere else.
										 // idea: immutableMap containingLookups to "default" values. Would allow use to take
										 // this out of the complicated enum constructors. hasDefaultValue() -> map.containsKey(key)
										 // then all we would have to do is change the order of the last two if's in consolidateSingle()
										 // ... also introduce idea of terminal flags? and run time flags?
				String val = consolidateSingle(opt);
				optMap.put(opt, val);
			}
		}
		return optMap;
	}

	/**
	 * 
	 * @param option
	 *            the option we are consolidating
	 * @return the consolidated result
	 */
	private String consolidateSingle(AppXpressOption option) {
		String input = null;
		String propVal = null;
		if(option.isStoreableProperty()) {
			propVal = properties.getProperty(option.getLongName());
		}
		if (userArgs.containsKey(option)) {
			input = userArgs.get(option);
			storeIfAppropriate(option, input);
			return input;
		} else if (propVal != null && !propVal.isEmpty()) {
			return propVal;
		} else if (option.isAppXpressMandatory()) {
			input = getParameterFromUser(option);
			storeIfAppropriate(option, input);
			return input;
		} else {
			return option.getDefaultValue();
		}
	}

	private void storeIfAppropriate(AppXpressOption option, String input) {
		if(option.isStoreableProperty()) {
			properties.put(option.getLongName(), input);
		}
	}

	/**
	 * @param option
	 *            The ExtractorOption to be query the user for.
	 * @return The value entered by the user.
	 */
	private String getParameterFromUser(AppXpressOption option) {
		String val = asker.ask(option.getMessage());
		while (!option.isValid(val)) {
			val = asker.ask(Asker.INVALID_INPUT);
		}
		return val;
	}

}
