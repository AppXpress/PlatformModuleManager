package com.gtnexus.appxpress.context;

import com.gtnexus.appxpress.PMProperties;
import com.gtnexus.appxpress.cli.option.AppXpressOption;
import com.gtnexus.appxpress.cli.option.BuilderOptionInterpreter;
import com.gtnexus.appxpress.cli.option.CLIOptionInterpreter;
import com.gtnexus.appxpress.cli.option.ParsedOptions;
import com.gtnexus.appxpress.pmbuilder.ApplicationInfo;
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
	public static <T extends Enum<T> & AppXpressOption> CLIOptionInterpreter<T> createInterpreter(ApplicationInfo app, SimpleShutdown shutdown, ParsedOptions<T> options, PMProperties properties) {
		if (app.getContextType().equals(BuilderOption.class)) {
			return (CLIOptionInterpreter<T>) new BuilderOptionInterpreter(app, shutdown,
					(ParsedOptions<BuilderOption>) options, properties);
		}
		throw new IllegalArgumentException("Unsupported context type.");
	}

}
