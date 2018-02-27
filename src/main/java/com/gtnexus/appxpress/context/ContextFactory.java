package com.gtnexus.appxpress.context;

import java.util.Map;
import java.util.Set;

import com.gtnexus.appxpress.AppXpressDirResolver;
import com.gtnexus.appxpress.cli.option.CLICommandOption;
import com.gtnexus.appxpress.cli.option.CLICommandOptionParser;
import com.gtnexus.appxpress.cli.option.CLICommandOptionInterpreter;
import com.gtnexus.appxpress.cli.option.ParsedOptions;
import com.gtnexus.appxpress.commons.DirectoryHelper;
import com.gtnexus.appxpress.commons.command.PMMCommandInfo;
import com.gtnexus.appxpress.commons.properties.PMProperties;
import com.gtnexus.appxpress.commons.runtime.SimpleShutdown;
import com.gtnexus.appxpress.commons.runtime.SimpleShutdownImpl;
import com.gtnexus.appxpress.exception.AppXpressException;

public class ContextFactory {

    private final AppXpressDirResolver resolver;
    private final InterpreterFactory interpreterFac;

    public ContextFactory() {
	this.resolver = new AppXpressDirResolver();
	this.interpreterFac = new InterpreterFactory(resolver);
    }

    public <M extends CLICommandOption> PmmContext<M> createContext(PMMCommandInfo app, String[] args)
	    throws AppXpressException {
	DirectoryHelper dHelper = new DirectoryHelper();
	dHelper.ensureAppXpress();
	PMProperties pmProperties = dHelper.getPmProperties();
	Set<CLICommandOption> options = app.getOptions();
	CLICommandOptionParser parser = CLICommandOptionParser.createParser(options, args);
	ParsedOptions parsedOptions = parser.parse();
	SimpleShutdown shutdown = new SimpleShutdownImpl();
	CLICommandOptionInterpreter interpreter = interpreterFac.createInterpreter(app, parsedOptions,
		pmProperties);
	Map<CLICommandOption, String> interpretedOptions = interpreter.interpret();
	return new PmmContext<>(app, shutdown, dHelper, parser.getOptions(), pmProperties, interpretedOptions);
    }

}
