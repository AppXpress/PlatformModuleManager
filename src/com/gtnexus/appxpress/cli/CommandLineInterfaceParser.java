package com.gtnexus.appxpress.cli;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.gtnexus.appxpress.pmextractor.exception.PMExtractorException;

/**
 * Wrapper to tie together CLIOptions and the Apache Commons CLI library.
 * 
 * @author jdonovan
 *
 */
public class CommandLineInterfaceParser<T extends CLIOption> {

	private final String appName;
	private final String[] userArgs;
	private final Set<T> cliOptionSet;
	private final Options options;
	private String helpHeader;
	private String helpFooter;
	private CommandLine cmd;

	/**
	 * 
	 * @param userArgs
	 *            the args provided to main() by the user when invoking this
	 *            tool
	 * @param cliOptionSet
	 *            the option set defining what can be passed to this tool
	 */
	public CommandLineInterfaceParser(String appName, String[] userArgs, Set<T> cliOptionSet) {
		if (userArgs == null || cliOptionSet == null) {
			throw new NullPointerException(
					"Cannot parse null args, or null option set.");
		}
		this.appName = appName;
		this.userArgs = userArgs;
		this.cliOptionSet = cliOptionSet;
		this.options = new Options();
		for (CLIOption opt : cliOptionSet) {
			options.addOption(Option.builder(opt.getFlag())
					.longOpt(opt.getLongName())
					.type(opt.getType())
					.desc(opt.getDescription())
					.hasArg(opt.hasArg())
					.required(false)
					.build());
		}
	}

	/**
	 * 
	 * @return CommandLine created from user input and the option set.
	 * @throws PMExtractorException
	 *             if the CommandLine is not parsable.
	 */
	public CommandLine getCommandLine() throws PMExtractorException {
		if (cmd == null) {
			parseCommandLine();
		}
		return cmd;
	}

	/**
	 * Parses the args provided by the user according to the provided option
	 * set.
	 * 
	 * @throws PMExtractorException
	 *             if input is not parasable.
	 */
	public void parseCommandLine() throws PMExtractorException {
		CommandLineParser parser = new DefaultParser();
		try {
			cmd = parser.parse(options, userArgs);
		} catch (ParseException e) {
			throw new PMExtractorException(
					"Failed to parse args from command line!\n"
							+ e.getMessage());
		}
	}

	/**
	 * Checks to see if the user provided this CLIOption.
	 * 
	 * @param opt
	 *            the CLIOption being checked for.
	 * @return false if parsing has not yet been performed.
	 */
	public boolean hasOption(CLIOption opt) {
		if (cmd == null) {
			return false;
		}
		return cmd.hasOption(opt.getLongName()) || cmd.hasOption(opt.getFlag());
	}

	public Map<T, String> getOptionsMap() {
		if (cmd == null || cmd.getOptions().length == 0) {
			return Collections.emptyMap();
		}
		Map<T, String> optMap = new HashMap<>();
		for (T opt : cliOptionSet) {
			if (cmd.hasOption(opt.getLongName())) {
				optMap.put(opt, cmd.getOptionValue(opt.getLongName()));
			}
		}
		return optMap;
	}

	public Set<T> getCliOptionSet() {
		return cliOptionSet;
	}

	/**
	 * Displays the usage information and exits.
	 */
	public void displayHelpAndExit() {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp(appName, helpHeader, options, helpFooter);
		System.exit(0);
	}

	public String getHelpHeader() {
		return helpHeader;
	}

	public void setHelpHeader(String helpHeader) {
		this.helpHeader = helpHeader;
	}

	public String getHelpFooter() {
		return helpFooter;
	}

	public void setHelpFooter(String helpFooter) {
		this.helpFooter = helpFooter;
	}

}
