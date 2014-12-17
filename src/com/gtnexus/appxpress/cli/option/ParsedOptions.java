package com.gtnexus.appxpress.cli.option;

import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.cli.CommandLine;

public class ParsedOptions<T extends Enum<T> & CLIOption> {

	public static <M extends Enum<M> & CLIOption> ParsedOptions<M> createFrom(
			CommandLine cmd, Set<M> optSet) {
		Map<M, String> optMap;
		boolean helpFlagIsSet = false;
		if (cmd == null || cmd.getOptions().length == 0) {
			optMap = Collections.emptyMap();
			return new ParsedOptions<M>(optMap, optSet);
		}
		optMap = new HashMap<>();
		for (M opt : optSet) {
			if (cmd.hasOption(opt.getLongName())) {
				optMap.put(opt, cmd.getOptionValue(opt.getLongName()));
				if (opt.isHelpFlag()) {
					helpFlagIsSet = true;
				}
			}
		}
		return new ParsedOptions<M>(optMap, helpFlagIsSet, optSet);
	}

	private final Map<T, String> optionsMap;
	private final boolean helpFlagIsSet;
	private final Set<T> optSet;

	public ParsedOptions(Map<T, String> optionsMap, Set<T> optSet) {
		this(optionsMap, false, optSet);
	}

	public ParsedOptions(Map<T, String> optionsMap, boolean helpFlagIsSet,
			Set<T> optSet) {
		this.optionsMap = optionsMap;
		this.helpFlagIsSet = false;
		this.optSet = optSet;
	}

	public Map<T, String> getOptionsMap() {
		return optionsMap;
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

}
