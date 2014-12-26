package com.gtnexus.appxpress.cli.option;

import java.util.Map;

import org.apache.commons.cli.HelpFormatter;

import com.gtnexus.appxpress.AppXpressException;
import com.gtnexus.appxpress.PMProperties;
import com.gtnexus.appxpress.context.SimpleShutdown;
import com.gtnexus.appxpress.pmbuilder.ApplicationInfo;
import com.gtnexus.appxpress.pmextractor.cli.CLIOptsAndPropConsolidator;

public abstract class AppXpressOptionInterpreter<T extends Enum<T> & AppXpressOption>
		implements CLIOptionInterpreter<T> {

	private ParsedOptions<T> parsedOptions;
	private final ApplicationInfo app;
	protected final SimpleShutdown shutdown;
	protected final PMProperties properties;

	public AppXpressOptionInterpreter(ApplicationInfo app,
			SimpleShutdown shutdown, ParsedOptions<T> parsedOptions,
			PMProperties properties) {
		this.app = app;
		this.shutdown = shutdown;
		this.parsedOptions = parsedOptions;
		this.properties = properties;
	}

	public final Map<T, String> interpret() throws AppXpressException {
		if (parsedOptions.isHelpFlagSet()) {
			HelpFormatter helpFormatter = new HelpFormatter();
			helpFormatter.printHelp(app.getAppName(), app.getHelpHeader(),
					parsedOptions.getOptions(), app.getHelpFooter());
			shutdown.shutdown();
		}
		parsedOptions = customInterpretation(parsedOptions);
		CLIOptsAndPropConsolidator<T> consolidator = new CLIOptsAndPropConsolidator<>(
				parsedOptions.getOptionsMap(), parsedOptions.getCliOptionSet(),
				properties);
		Map<T, String> optMap = consolidator.consolidate();
		return optMap;
	}

	public abstract ParsedOptions<T> customInterpretation(
			ParsedOptions<T> parsedOpts) throws AppXpressException;

}
