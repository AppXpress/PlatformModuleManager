package com.gtnexus.appxpress.cli.option;

import java.util.Map;

import com.gtnexus.appxpress.AppXpressException;
import com.gtnexus.appxpress.PMProperties;
import com.gtnexus.appxpress.context.AppXpressContext;
import com.gtnexus.appxpress.pmextractor.cli.CLIOptsAndPropConsolidator;

public abstract class AppXpressOptionInterpreter<T extends Enum<T> & AppXpressOption>
		implements CLIOptionInterpreter<T> {

	private ParsedOptions<T> parsedOptions;

	public AppXpressOptionInterpreter(ParsedOptions<T> parsedOptions) {
		this.parsedOptions = parsedOptions;
	}

	public final Map<T, String> interpret() throws AppXpressException {
		AppXpressContext<T> context = getContext();
		if (parsedOptions.isHelpFlagSet()) {
			// parser.displayHelpAndExit();
		}
		parsedOptions = customInterpretation(parsedOptions);
		PMProperties pmProperties = context.getPMProperties();
		CLIOptsAndPropConsolidator<T> consolidator = new CLIOptsAndPropConsolidator<>(
				parsedOptions.getOptionsMap(), parsedOptions.getCliOptionSet(),
				pmProperties.getProperties());
		Map<T, String> optMap = consolidator.consolidate();
		return optMap;
	}

	public AppXpressContext<T> getContext() {
		return null;
	}

	public abstract ParsedOptions<T> customInterpretation(ParsedOptions<T> parsedOpts) throws AppXpressException;

}
