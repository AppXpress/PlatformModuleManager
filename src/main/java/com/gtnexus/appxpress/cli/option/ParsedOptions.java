package com.gtnexus.appxpress.cli.option;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

import com.google.common.base.Preconditions;

public class ParsedOptions {

    public static ParsedOptions createFrom(CLICommandOptionParser parser) {
	return createFrom(parser.getCommandLine(), parser.getOptions(), parser.getCliOptionSet());
    }

    public static  ParsedOptions createFrom(CommandLine cmd, Options options, Set<CLICommandOption> optSet) {
	Preconditions.checkNotNull(cmd);
	Preconditions.checkNotNull(options);
	Preconditions.checkNotNull(optSet);
	Map<CLICommandOption, String> optMap;
	boolean helpFlagIsSet = false;
	optMap = new HashMap<>();
	for (CLICommandOption opt : optSet) {
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
    private final Map<CLICommandOption, String> optionsMap;
    private final boolean helpFlagIsSet;
    private final Set<CLICommandOption> optSet;

    public ParsedOptions(Options options, Map<CLICommandOption, String> optionsMap, Set<CLICommandOption> optSet) {
	this(options, optionsMap, false, optSet);
    }

    public ParsedOptions(Options options, Map<CLICommandOption, String> optionsMap, boolean helpFlagIsSet, Set<CLICommandOption> optSet) {
	this.options = options;
	this.optionsMap = optionsMap;
	this.helpFlagIsSet = helpFlagIsSet;
	this.optSet = optSet;
    }

    public Map<CLICommandOption, String> getOptionsMap() {
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
    public boolean hasOption(CLICommandOption opt) {
	return optionsMap.containsKey(opt);
    }

    public String getOption(CLICommandOption opt) {
	return optionsMap.get(opt);
    }

    public Set<CLICommandOption> getCliOptionSet() {
	return optSet;
    }

    /**
     * 
     * @return whether or not the parsed parameters contain the help flag
     */
    public boolean isHelpFlagSet() {
	return helpFlagIsSet;
    }

    public void put(CLICommandOption opt, String val) {
	optionsMap.put(opt, val);
    }

    public void put(CLICommandOption opt, Path val) {
	put(opt, val.getFileName().toString());
    }

    public void put(CLICommandOption opt, File val) {
	put(opt, val.getName());
    }

}
