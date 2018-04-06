package com.gtnexus.pmm.cli.option;

import java.util.Set;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.gtnexus.pmm.AppXpressException;
import com.gtnexus.pmm.pmextractor.exception.PMExtractorException;

/**
 * Wrapper to tie together CLICommandOptions and the Apache Commons CLI library.
 * 
 * @author jdonovan
 *
 */
public class CommandOptionParser {

    private final Set<CommandOption> cliOptionSet;
    private final Options options;
    private CommandLine cmd;

    public static CommandOptionParser createParser(Set<CommandOption> cliOptSet) {
	Options options = new Options();
	for (CommandOption opt : cliOptSet) {
	    Option o = Option
		    .builder(opt.getFlag())
		    .longOpt(opt.getLongName())
		    .type(opt.getType())
		    .desc(opt.getDescription())
		    .hasArg(opt.hasArg())
		    .required(false)
		    .build();
	    options.addOption(o);
	}
	return new CommandOptionParser(cliOptSet, options);
    }

    /**
     * 
     * @param userArgs
     *            the args provided to main() by the user when invoking this tool
     * @param cliOptionSet
     *            the option set defining what can be passed to this tool
     */
    private CommandOptionParser(Set<CommandOption> cliOptionSet, Options options) {
	if (cliOptionSet == null) {
	    throw new NullPointerException("Cannot parse for null option set.");
	}
	this.cliOptionSet = cliOptionSet;
	this.options = options;
    }

    /**
     * Parses the args provided by the user according to the provided option set.
     * 
     * @throws PMExtractorException
     *             if input is not parasable.
     */
    public ParsedOptions parse(String[] userArgs) throws AppXpressException {
	CommandLineParser parser = new DefaultParser();
	try {
	    cmd = parser.parse(options, userArgs);
	} catch (ParseException e) {
	    throw new AppXpressException("Failed to parse args from command line!\n" + e.getMessage());
	}
	return ParsedOptions.createFrom(this);
    }

    public CommandLine getCommandLine() {
	return cmd;
    }

    public Set<CommandOption> getCliOptionSet() {
	return cliOptionSet;
    }

    public Options getOptions() {
	return options;
    }

}
