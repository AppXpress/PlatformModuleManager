package com.gtnexus.appxpress.context;

import com.gtnexus.appxpress.cli.option.AppXpressOption;
import com.gtnexus.appxpress.cli.option.BuilderOptionInterpreter;
import com.gtnexus.appxpress.cli.option.CLIOptionInterpreter;
import com.gtnexus.appxpress.cli.option.ParsedOptions;
import com.gtnexus.appxpress.pmbuilder.cli.BuilderOption;

public class InterpreterFactory {

	/**
	 * 
	 * @param contextType
	 * @param options
	 * @throws IllegalArgumentException if contextType is not supported.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Enum<T> & AppXpressOption> CLIOptionInterpreter<T> createInterpreter(
			Class<T> contextType, ParsedOptions<T> options) {
		if (contextType.equals(BuilderOption.class)) {
			return (CLIOptionInterpreter<T>) new BuilderOptionInterpreter(
					(ParsedOptions<BuilderOption>) options);
		}
		throw new IllegalArgumentException("Unsupported context type.");
	}

}
