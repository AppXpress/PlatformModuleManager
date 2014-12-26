package com.gtnexus.appxpress.context;

import static com.gtnexus.appxpress.AppXpressConstants.PROPERTIES_EXTENSION;

import java.util.Map;

import com.gtnexus.appxpress.AppXpressDirResolver;
import com.gtnexus.appxpress.AppXpressException;
import com.gtnexus.appxpress.DirectoryHelper;
import com.gtnexus.appxpress.PMProperties;
import com.gtnexus.appxpress.cli.option.AppXpressOption;
import com.gtnexus.appxpress.cli.option.CLIOptionInterpreter;
import com.gtnexus.appxpress.cli.option.CLIOptionParser;
import com.gtnexus.appxpress.cli.option.ParsedOptions;
import com.gtnexus.appxpress.pmbuilder.ApplicationInfo;
import com.gtnexus.appxpress.pmextractor.cli.CLIOptsAndPropConsolidator;

public class ContextFactory {

	AppXpressDirResolver resolver;

	public ContextFactory() {
		this.resolver = new AppXpressDirResolver();
	}

	public <M extends Enum<M> & AppXpressOption> AppXpressContext<M> creatContext(
			ApplicationInfo app, String[] args) throws AppXpressException {
		DirectoryHelper dHelper = new DirectoryHelper(app.getAppName()
				+ PROPERTIES_EXTENSION);
		dHelper.ensureAppXpress();
		PMProperties pmProperties = dHelper.getPmProperties();
		Class<M> contextType = app.getContextType();
		CLIOptionParser<M> parser = CLIOptionParser.createParser(
				app.getAppName(), args, contextType);
		ParsedOptions<M> parsedOptions = parser.parse();
		SimpleShutdown shutdown = new SimpleShutdownImpl();
		CLIOptionInterpreter<M> interpreter = InterpreterFactory
				.createInterpreter(app, shutdown, parsedOptions, pmProperties);
		Map<M, String> interpretedOptions = interpreter.interpret();
		CLIOptsAndPropConsolidator<M> consolidator = new CLIOptsAndPropConsolidator<>(
				interpretedOptions, parsedOptions.getCliOptionSet(),
				pmProperties);
		Map<M, String> optMap = consolidator.consolidate();
		return new AppXpressContext<>(app.getAppName(), contextType, shutdown,
				dHelper, parser.getOptions(), pmProperties, optMap);
	}

}
