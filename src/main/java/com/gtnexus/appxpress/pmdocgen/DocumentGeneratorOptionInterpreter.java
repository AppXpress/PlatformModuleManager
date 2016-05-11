package com.gtnexus.appxpress.pmdocgen;

import com.gtnexus.appxpress.Exception.AppXpressException;
import com.gtnexus.appxpress.cli.option.CLICommandOptionInterpreter;
import com.gtnexus.appxpress.cli.option.CLIOptionInterpreter;
import com.gtnexus.appxpress.cli.option.ParsedOptions;
import com.gtnexus.appxpress.commons.command.PMMCommandInfo;
import com.gtnexus.appxpress.commons.properties.PMProperties;
import com.gtnexus.appxpress.commons.runtime.SimpleShutdown;
import com.gtnexus.appxpress.pmdocgen.cli.option.DocumentGeneratorOptions;

public class DocumentGeneratorOptionInterpreter extends CLICommandOptionInterpreter<DocumentGeneratorOptions>
		implements CLIOptionInterpreter<DocumentGeneratorOptions> {

	public DocumentGeneratorOptionInterpreter(PMMCommandInfo app, SimpleShutdown shutdown,
			ParsedOptions<DocumentGeneratorOptions> parsedOptions, PMProperties properties) {
		super(app, shutdown, parsedOptions, properties);
	}

	@Override
	protected ParsedOptions<DocumentGeneratorOptions> performCustomInterpretation(
			ParsedOptions<DocumentGeneratorOptions> parsedOpts) throws AppXpressException {
		return parsedOpts; //no-op for now.
	}

}
