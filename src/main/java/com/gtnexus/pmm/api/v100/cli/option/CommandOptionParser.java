package com.gtnexus.pmm.api.v100.cli.option;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.gtnexus.pmm.api.v100.command.SubCommandException;
import com.gtnexus.pmm.common.CommandOptionTransformers;
import com.gtnexus.pmm.extract.exception.PMExtractorException;

/**
 * Wrapper to tie together CLICommandOptions and the Apache Commons CLI library.
 * 
 * @author jdonovan
 *
 */
public class CommandOptionParser {

    private final Set<CommandOption> cliOptionSet;
    private final Options options;

    public static CommandOptionParser createParser(Set<CommandOption> cliOptSet) {
	Options options = CommandOptionTransformers.transformToOptions(cliOptSet);
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
    public Map<CommandOption, String> parse(String[] userArgs) throws SubCommandException {
	CommandLineParser parser = new DefaultParser();
	try {
	    return transform(parser.parse(options, userArgs));
	} catch (ParseException e) {
	    throw new SubCommandException("Failed to parse args from command line!\n" + e.getMessage());
	}
    }

    public Set<CommandOption> getCliOptionSet() {
	return cliOptionSet;
    }

    public Options getOptions() {
	return options;
    }

    private Map<CommandOption, String> transform(CommandLine apacheParseResult) {
	Map<CommandOption, String> optMap = new HashMap<>();
	for(CommandOption opt: cliOptionSet) {
	    if (apacheParseResult.hasOption(opt.getLongName()) || apacheParseResult.hasOption(opt.getFlag())) {
		optMap.put(opt, apacheParseResult.getOptionValue(opt.getLongName()));
	    }
	}
	return optMap;
    }

}
