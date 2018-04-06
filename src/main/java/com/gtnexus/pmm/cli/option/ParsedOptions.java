package com.gtnexus.pmm.cli.option;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

import com.google.common.base.Preconditions;

public class ParsedOptions {

    public static ParsedOptions createFrom(CommandOptionParser parser) {
	return createFrom(parser.getCommandLine(), parser.getOptions(), parser.getCliOptionSet());
    }

    public static  ParsedOptions createFrom(CommandLine cmd, Options options, Set<CommandOption> optSet) {
	Preconditions.checkNotNull(cmd);
	Preconditions.checkNotNull(options);
	Preconditions.checkNotNull(optSet);
	Map<CommandOption, String> optMap;
	boolean helpFlagIsSet = false;
	optMap = new HashMap<>();
	for (CommandOption opt : optSet) {
	    if (cmd.hasOption(opt.getLongName()) || cmd.hasOption(opt.getFlag())) {
		optMap.put(opt, cmd.getOptionValue(opt.getLongName()));
		if (opt.isHelpFlag()) {
		    helpFlagIsSet = true;
		}
	    }
	}
	return new ParsedOptions(options, optMap, helpFlagIsSet, optSet);
    }

    // ---------------------------------------------------------------------------------------------

    private final Options options;
    private final Map<CommandOption, String> optionsMap;
    private final boolean helpFlagIsSet;
    private final Set<CommandOption> optSet;

    public ParsedOptions(Options options, Map<CommandOption, String> optionsMap, Set<CommandOption> optSet) {
	this(options, optionsMap, false, optSet);
    }

    public ParsedOptions(Options options, Map<CommandOption, String> optionsMap, boolean helpFlagIsSet, Set<CommandOption> optSet) {
	this.options = options;
	this.optionsMap = optionsMap;
	this.helpFlagIsSet = helpFlagIsSet;
	this.optSet = optSet;
    }

    public Map<CommandOption, String> getOptionsMap() {
	return optionsMap;
    }

    public Options getOptions() {
	return options;
    }

    /**
     * Checks to see if the user provided this CLICommandOption.
     * 
     * @param opt
     *            the CLICommandOption being checked for.
     * @return false if parsing has not yet been performed.
     */
    public boolean hasOption(CommandOption opt) {
	return optionsMap.containsKey(opt);
    }

    public String getOption(CommandOption opt) {
	return optionsMap.get(opt);
    }

    public Set<CommandOption> getCliOptionSet() {
	return optSet;
    }

    /**
     * 
     * @return whether or not the parsed parameters contain the help flag
     */
    public boolean isHelpFlagSet() {
	return helpFlagIsSet;
    }

    public void put(CommandOption opt, String val) {
	optionsMap.put(opt, val);
    }

    public void put(CommandOption opt, Path val) {
	put(opt, val.getFileName().toString());
    }

    public void put(CommandOption opt, File val) {
	put(opt, val.getName());
    }

}
