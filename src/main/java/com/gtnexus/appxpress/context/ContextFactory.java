package com.gtnexus.appxpress.context;

import static com.gtnexus.appxpress.AppXpressConstants.PROPERTIES_EXTENSION;

import java.util.Map;
import java.util.Set;

import com.gtnexus.appxpress.AppXpressDirResolver;
import com.gtnexus.appxpress.Exception.AppXpressException;
import com.gtnexus.appxpress.cli.CLIOptsAndPropConsolidator;
import com.gtnexus.appxpress.cli.option.CLICommandOption;
import com.gtnexus.appxpress.cli.option.CLIOptionInterpreter;
import com.gtnexus.appxpress.cli.option.CLIOptionParser;
import com.gtnexus.appxpress.cli.option.ParsedOptions;
import com.gtnexus.appxpress.commons.DirectoryHelper;
import com.gtnexus.appxpress.commons.command.PMMCommandInfo;
import com.gtnexus.appxpress.commons.properties.PMProperties;
import com.gtnexus.appxpress.commons.runtime.SimpleShutdown;
import com.gtnexus.appxpress.commons.runtime.SimpleShutdownImpl;

public class ContextFactory {

	private final AppXpressDirResolver resolver;
	private final InterpreterFactory interpreterFac;

	public ContextFactory() {
		this.resolver = new AppXpressDirResolver();
		this.interpreterFac = new InterpreterFactory(resolver);
	}

	public <M extends CLICommandOption> AppXpressContext<M> createContext(
			PMMCommandInfo app, String[] args) throws AppXpressException {
		DirectoryHelper dHelper = new DirectoryHelper(app.getName() // TODO: would like to move this,
									+ PROPERTIES_EXTENSION);		// but should wait till we're ready to rename the properties file.
		dHelper.ensureAppXpress();
		PMProperties pmProperties = dHelper.getPmProperties();
		Set<M> options = app.getOptions();
		CLIOptionParser<M> parser = CLIOptionParser.createParser(options, args);
		ParsedOptions<M> parsedOptions = parser.parse();
		SimpleShutdown shutdown = new SimpleShutdownImpl();
		CLIOptionInterpreter<M> interpreter = interpreterFac.createInterpreter(
				app, shutdown, parsedOptions, pmProperties);
		Map<M, String> interpretedOptions = interpreter.interpret();
		CLIOptsAndPropConsolidator<M> consolidator = new CLIOptsAndPropConsolidator<>(
				interpretedOptions, parsedOptions.getCliOptionSet(),
				pmProperties);
		Map<M, String> optMap = consolidator.consolidate();
		return new AppXpressContext<>(app, shutdown,
				dHelper, parser.getOptions(), pmProperties, optMap);
	}

}
