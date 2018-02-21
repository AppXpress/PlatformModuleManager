package com.gtnexus.appxpress.cli;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.gtnexus.appxpress.cli.asker.Asker;
import com.gtnexus.appxpress.cli.asker.SimpleAsker;
import com.gtnexus.appxpress.cli.option.CLICommandOption;
import com.gtnexus.appxpress.commons.properties.PMProperties;

/**
 * Consolidates stored properties file from User, with flags passed from CLI.
 * <p>
 * Flags passed from the CLI take precedence over what is stored in the
 * properties file. If neither is present for a mandatory ExtractorOption, then
 * the user is prompted for input.
 *
 * @author jjdonov
 */
public class CLIOptsAndPropConsolidator<T extends CLICommandOption> {

    private final Map<T, String> userArgs;
    private final Set<T> optSet;
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
    public CLIOptsAndPropConsolidator(Map<T, String> userArgs, Set<T> optSet, PMProperties properties) {
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
    public CLIOptsAndPropConsolidator(Map<T, String> userArgs, Set<T> optSet, PMProperties properties,
	    InputStream inputStream, PrintStream printStream) {
	this.userArgs = userArgs;
	this.optSet = optSet;
	this.properties = properties;
	this.asker = new SimpleAsker(inputStream, printStream);
    }

    /**
     * @return the map of consolidated ExtractorOptions and their values.
     */
    public Map<T, String> consolidate() {
	final Map<T, String> optMap = new HashMap<>();
	for (T opt : optSet) {
	    if (!opt.shouldBeOmitted()) {
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
    private String consolidateSingle(CLICommandOption option) {
	String input = null;
	String propVal = null;
	if (option.isStoreableProperty()) {
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

    private void storeIfAppropriate(CLICommandOption option, String input) {
	if (option.isStoreableProperty()) {
	    properties.put(option.getLongName(), input);
	}
    }

    /**
     * @param option
     *            The ExtractorOption to be query the user for.
     * @return The value entered by the user.
     */
    private String getParameterFromUser(CLICommandOption option) {
	String val = asker.ask(option.getMessage());
	while (!option.isValid(val)) {
	    val = asker.ask(Asker.INVALID_INPUT);
	}
	return val;
    }

}
