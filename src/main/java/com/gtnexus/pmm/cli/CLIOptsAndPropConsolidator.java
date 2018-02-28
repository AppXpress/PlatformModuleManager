package com.gtnexus.pmm.cli;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.gtnexus.pmm.cli.asker.Asker;
import com.gtnexus.pmm.cli.asker.SimpleAsker;
import com.gtnexus.pmm.cli.option.CLICommandOption;
import com.gtnexus.pmm.commons.properties.PMProperties;

/**
 * Consolidates stored properties file from User, with flags passed from CLI.
 * <p>
 * Flags passed from the CLI take precedence over what is stored in the
 * properties file. If neither is present for a mandatory ExtractorOption, then
 * the user is prompted for input.
 *
 * @author jjdonov
 */
public class CLIOptsAndPropConsolidator {

    private final Map<CLICommandOption, String> userArgs;
    private final Set<CLICommandOption> optSet;
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
    public CLIOptsAndPropConsolidator(Map<CLICommandOption, String> userArgs, Set<CLICommandOption> optSet, PMProperties properties) {
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
     *            CLICommandOptionhe input stream that this consolidator should read from.
     * @param printStream
     *            CLICommandOptionhe printStream that this consolidator should write to.
     */
    public CLIOptsAndPropConsolidator(Map<CLICommandOption, String> userArgs, Set<CLICommandOption> optSet, PMProperties properties, InputStream inputStream, PrintStream printStream) {
	this.userArgs = userArgs;
	this.optSet = optSet;
	this.properties = properties;
	this.asker = new SimpleAsker(inputStream, printStream);
    }

    /**
     * @return the map of consolidated ExtractorOptions and their values.
     */
    public Map<CLICommandOption, String> consolidate() {
	final Map<CLICommandOption, String> optMap = new HashMap<>();
	for (CLICommandOption opt : optSet) {
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
     *            CLICommandOptionhe ExtractorOption to be query the user for.
     * @return CLICommandOptionhe value entered by the user.
     */
    private String getParameterFromUser(CLICommandOption option) {
	String val = asker.ask(option.getMessage());
	while (!option.isValid(val)) {
	    val = asker.ask(Asker.INVALID_INPUT);
	}
	return val;
    }

}
