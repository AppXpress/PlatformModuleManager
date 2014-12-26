package com.gtnexus.appxpress.pmextractor.cli;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.gtnexus.appxpress.AppXpressException;
import com.gtnexus.appxpress.PMProperties;
import com.gtnexus.appxpress.cli.Asker;
import com.gtnexus.appxpress.cli.SimpleAsker;
import com.gtnexus.appxpress.cli.option.AppXpressOption;
import com.gtnexus.appxpress.pmextractor.exception.PMExtractorException;

/**
 * Consolidates stored properties file from User, with flags passed from CLI.
 * <p>
 * Flags passed from the CLI take precedence over what is stored in the
 * properties file. If neither is present for a mandatory ExtractorOption, then
 * the user is prompted for input.
 *
 * @author jjdonov
 */
public class CLIOptsAndPropConsolidator<T extends AppXpressOption> {

	private final Map<T, String> userArgs;
	private final Set<T> optSet;
	private final PMProperties properties;
	private final SimpleAsker asker;
	private boolean presentSave;

	/**
	 * 
	 * @param userArgs
	 *            Arguments passed from the CLI.
	 * @param optSet
	 * @param properties
	 *            Properties file read from user's AppXpress directory.
	 */
	public CLIOptsAndPropConsolidator(Map<T, String> userArgs,
			Set<T> optSet, PMProperties properties) {
		this.userArgs = userArgs;
		this.optSet = optSet;
		this.properties = properties;
		this.asker = new SimpleAsker(System.in, System.out);
		presentSave = false;
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
	public CLIOptsAndPropConsolidator(Map<T, String> userArgs,
			Set<T> optSet, PMProperties properties, InputStream inputStream,
			PrintStream printStream) {
		this.userArgs = userArgs;
		this.optSet = optSet;
		this.properties = properties;
		this.asker = new SimpleAsker(inputStream, printStream);
		presentSave = false;
	}

	/**
	 * @return the map of consolidated ExtractorOptions and their values.
	 */
	public Map<T, String> consolidate() {
		final Map<T, String> optMap = new HashMap<>();
		for (T opt : optSet) {
			if(!opt.shouldBeOmitted()) {
				String val = consolidateSingle(opt);
				optMap.put(opt, val);
				if(opt.isStoreableProperty()) {
					properties.put(opt.getLongName(), val);
				}
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
			if(option.isStoreableProperty()) {
				presentSave = true;
			}
		}
		if (input != null && !input.isEmpty()) {
			return input;
		} else if (propVal != null && !propVal.isEmpty()) {
			return propVal;
		} else if (option.isAppXpressMandatory()) {
			return getParameterFromUser(option);
		} else {
			return option.getDefaultValue();
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

	
	//TOOD this has nothing to do with the consolidation.
	/**
	 * @param propPath
	 *            the path to the Properties file to be written to
	 * @throws PMExtractorException
	 *             when there is an IOException when writing to the properties
	 *             file at the propPath.
	 */
	public void presentSaveOption(final String propPath) throws AppXpressException {
		if(presentSave) {
			final String answer = askSaveQuestion();
			if (answer.equalsIgnoreCase("Y")) {
				saveProps();
			}
		}
	}
	
	/**
	 * 
	 * @throws PMExtractorException
	 */
	private void saveProps() throws AppXpressException {
		properties.store();
	}
	
	/**
	 * 
	 * @return
	 */
	private String askSaveQuestion() {
		String answer = asker.ask("Save settings? [y/n]: ");
		while (!answer.equalsIgnoreCase("Y") && !answer.equalsIgnoreCase("N")) {
			answer = asker.ask(Asker.INVALID_INPUT);
		}
		return answer;
	}
}
