package com.gtnexus.appxpress.cli.option;

import com.gtnexus.appxpress.AppXpressException;
import com.gtnexus.appxpress.commons.ApplicationInfo;
import com.gtnexus.appxpress.commons.PMProperties;
import com.gtnexus.appxpress.commons.SimpleShutdown;
import com.gtnexus.appxpress.pmextractor.cli.ExtractorOption;

public class ExtractorOptionInterpreter extends
		AppXpressOptionInterpreter<ExtractorOption> implements
		CLIOptionInterpreter<ExtractorOption> {

	public ExtractorOptionInterpreter(ApplicationInfo app,
			SimpleShutdown shutdown,
			ParsedOptions<ExtractorOption> parsedOptions,
			PMProperties properties) {
		super(app, shutdown, parsedOptions, properties);
	}

	@Override
	public ParsedOptions<ExtractorOption> customInterpretation(
			ParsedOptions<ExtractorOption> parsedOpts)
			throws AppXpressException {
		return parsedOpts; // do nothing!
	}
}
