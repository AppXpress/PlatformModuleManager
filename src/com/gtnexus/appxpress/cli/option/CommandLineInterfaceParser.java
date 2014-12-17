package com.gtnexus.appxpress.cli.option;

import java.util.EnumSet;
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
public class CommandLineInterfaceParser<T extends Enum<T> & CLIOption> {

	private final String appName;
	private final String[] userArgs;
	private final Set<T> cliOptionSet;
	private final Options options;
	private String helpHeader;
	private String helpFooter;
	private CommandLine cmd;

	public static <M extends Enum<M> & CLIOption> CommandLineInterfaceParser<M> createParser(
			String appName, String[] userArgs, Class<M> optClass) {
		Options options = new Options();
		Set<M> cliOptSet = EnumSet.allOf(optClass);
		for (CLIOption opt : cliOptSet) {
			options.addOption(Option.builder(opt.getFlag())
					.longOpt(opt.getLongName()).type(opt.getType())
					.desc(opt.getDescription()).hasArg(opt.hasArg())
					.required(false).build());
		}
		return new CommandLineInterfaceParser<M>(appName, userArgs, cliOptSet, options);
	}

	/**
	 * 
	 * @param userArgs
	 *            the args provided to main() by the user when invoking this
	 *            tool
	 * @param cliOptionSet
	 *            the option set defining what can be passed to this tool
	 */
	public CommandLineInterfaceParser(String appName, String[] userArgs,
			Set<T> cliOptionSet, Options options) {
		if (userArgs == null || cliOptionSet == null) {
			throw new NullPointerException(
					"Cannot parse null args, or null option set.");
		}
		this.appName = appName;
		this.userArgs = userArgs;
		this.cliOptionSet = cliOptionSet;
		this.options = options;
	}

	/**
	 * 
	 * @return CommandLine created from user input and the option set.
	 * @throws PMExtractorException
	 *             if the CommandLine is not parsable.
	 */
	public CommandLine getCommandLine() throws PMExtractorException {
		if (cmd == null) {
			parse();
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
	public ParsedOptions<T> parse() throws PMExtractorException {
		CommandLineParser parser = new DefaultParser();
		try {
			cmd = parser.parse(options, userArgs);
		} catch (ParseException e) {
			throw new PMExtractorException(
					"Failed to parse args from command line!\n"
							+ e.getMessage());
		}
		return ParsedOptions.createFrom(cmd, cliOptionSet);
	}


	public Set<T> getCliOptionSet() {
		return cliOptionSet;
	}

	public String getAppName() {
		return appName;
	}

	// TODO: REFACTOR ME! THIS HAS NOTHING TO DO WITH PARSING
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
