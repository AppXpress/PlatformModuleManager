package com.gtnexus.appxpress.cli.option;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

public class ParsedOptions {

	public static ParsedOptions createFrom(
			CommandLine cmd, Options options, Set<AppXpressOption> optSet) {
		Map<AppXpressOption, String> optMap;
		boolean helpFlagIsSet = false;
		optMap = new HashMap<>();
		if (cmd == null || cmd.getOptions().length == 0) {
			return new ParsedOptions(options, optMap, optSet);
		}
		for (AppXpressOption opt : optSet) {
			if (cmd.hasOption(opt.getLongName()) || cmd.hasOption(opt.getFlag())) {
				optMap.put(opt, cmd.getOptionValue(opt.getLongName()));
				if (opt.isHelpFlag()) {
					helpFlagIsSet = true;
				}
			}
		}
		return new ParsedOptions(options, optMap, helpFlagIsSet, optSet);
	}

	private final Options options;
	private final Map<AppXpressOption, String> optionsMap;
	private final boolean helpFlagIsSet;
	private final Set<AppXpressOption> optSet;

	public ParsedOptions(Options options, Map<AppXpressOption, String> optionsMap, Set<AppXpressOption> optSet) {
		this(options, optionsMap, false, optSet);
	}

	public ParsedOptions(Options options, Map<AppXpressOption, String> optionsMap, boolean helpFlagIsSet,
			Set<AppXpressOption> optSet) {
		this.options = options;
		this.optionsMap = optionsMap;
		this.helpFlagIsSet = helpFlagIsSet;
		this.optSet = optSet;
	}

	public Map<AppXpressOption, String> getOptionsMap() {
		return optionsMap;
	}
	
	public Options getOptions() {
		return options;
	}

	/**
	 * Checks to see if the user provided this CLIOption.
	 * 
	 * @param opt
	 *            the CLIOption being checked for.
	 * @return false if parsing has not yet been performed.
	 */
	public boolean hasOption(AppXpressOption opt) {
		return optionsMap.containsKey(opt);
	}
	
	public String getOption(AppXpressOption opt) {
		return optionsMap.get(opt);
	}

	public Set<AppXpressOption> getCliOptionSet() {
		return optSet;
	}
	
	/**
	 * 
	 * @return whether or not the parsed parameters contain the help flag
	 */
	public boolean isHelpFlagSet() {
		return helpFlagIsSet;
	}

	public void put(AppXpressOption opt, String val) {
		optionsMap.put(opt, val);
	}
	
	public void put(AppXpressOption opt, Path val) {
		put(opt, val.getFileName().toString());
	}
	
	public void put(AppXpressOption opt, File val) {
		put(opt, val.getName());
	}

}
