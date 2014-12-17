package com.gtnexus.appxpress.context;

import java.util.Map;

import com.gtnexus.appxpress.AppXpressException;
import com.gtnexus.appxpress.DirectoryHelper;
import com.gtnexus.appxpress.PMProperties;
import com.gtnexus.appxpress.cli.option.AppXpressOption;
import com.gtnexus.appxpress.cli.option.CLIOptionInterpreter;
import com.gtnexus.appxpress.cli.option.CommandLineInterfaceParser;
import com.gtnexus.appxpress.cli.option.ParsedOptions;
import com.gtnexus.appxpress.pmextractor.cli.CLIOptsAndPropConsolidator;

public class ContextFactory {

	public static <M extends Enum<M> & AppXpressOption> AppXpressContext<M> creatContext(
			String appName, Class<M> contextType, String[] args)
			throws AppXpressException {
		DirectoryHelper dHelper = new DirectoryHelper("");
		dHelper.ensureAppXpress();
		PMProperties pmProperties = dHelper.getPmProperties();
		CommandLineInterfaceParser<M> parser = CommandLineInterfaceParser
				.createParser(appName, args, contextType);
		ParsedOptions<M> parsedOptions = parser.parse();
		CLIOptionInterpreter<M> interpreter = InterpreterFactory.createInterpreter(contextType, parsedOptions);
		Map<M, String> interpretedOptions = interpreter.interpret();
		CLIOptsAndPropConsolidator<M> consolidator = new CLIOptsAndPropConsolidator<>(
				interpretedOptions, parsedOptions.getCliOptionSet(),
				pmProperties.getProperties());
		Map<M, String> optMap = consolidator.consolidate();
		return new AppXpressContext<>(appName, contextType, dHelper, optMap);
	}

}
