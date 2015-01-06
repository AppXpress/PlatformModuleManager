package com.gtnexus.appxpress.cli.option;

import java.io.File;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

public class ParsedOptions<T extends Enum<T> & CLIOption> {

	public static <M extends Enum<M> & CLIOption> ParsedOptions<M> createFrom(
			CommandLine cmd, Options options, Set<M> optSet) {
		Map<M, String> optMap;
		boolean helpFlagIsSet = false;
		if (cmd == null || cmd.getOptions().length == 0) {
			optMap = Collections.emptyMap();
			return new ParsedOptions<M>(options, optMap, optSet);
		}
		optMap = new HashMap<>();
		for (M opt : optSet) {
			if (cmd.hasOption(opt.getLongName()) || cmd.hasOption(opt.getFlag())) {
				optMap.put(opt, cmd.getOptionValue(opt.getLongName()));
				if (opt.isHelpFlag()) {
					helpFlagIsSet = true;
				}
			}
		}
		return new ParsedOptions<M>(options, optMap, helpFlagIsSet, optSet);
	}

	private final Options options;
	private final Map<T, String> optionsMap;
	private final boolean helpFlagIsSet;
	private final Set<T> optSet;

	public ParsedOptions(Options options, Map<T, String> optionsMap, Set<T> optSet) {
		this(options, optionsMap, false, optSet);
	}

	public ParsedOptions(Options options, Map<T, String> optionsMap, boolean helpFlagIsSet,
			Set<T> optSet) {
		this.options = options;
		this.optionsMap = optionsMap;
		this.helpFlagIsSet = helpFlagIsSet;
		this.optSet = optSet;
	}

	public Map<T, String> getOptionsMap() {
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
	public boolean hasOption(T opt) {
		return optionsMap.containsKey(opt);
	}

	public Set<T> getCliOptionSet() {
		return optSet;
	}

	/**
	 * 
	 * @return whether or not the parsed parameters contain the help flag
	 */
	public boolean isHelpFlagSet() {
		return helpFlagIsSet;
	}

	public void put(T opt, String val) {
		optionsMap.put(opt, val);
	}
	
	public void put(T opt, Path val) {
		put(opt, val.getFileName().toString());
	}
	
	public void put(T opt, File val) {
		put(opt, val.getName());
	}

}
