package com.gtnexus.pmm.context;

import java.util.Map;
import java.util.Set;

import com.gtnexus.pmm.AppXpressDirResolver;
import com.gtnexus.pmm.AppXpressException;
import com.gtnexus.pmm.cli.option.CLICommandOption;
import com.gtnexus.pmm.cli.option.CLICommandOptionInterpreter;
import com.gtnexus.pmm.cli.option.CLICommandOptionParser;
import com.gtnexus.pmm.cli.option.ParsedOptions;
import com.gtnexus.pmm.commons.DirectoryHelper;
import com.gtnexus.pmm.commons.command.PMMCommandInfo;
import com.gtnexus.pmm.commons.runtime.SimpleShutdown;
import com.gtnexus.pmm.commons.runtime.SimpleShutdownImpl;

public class ContextFactory {
    
    private ContextFactory() {}

    private static final AppXpressDirResolver resolver = new AppXpressDirResolver();
    private static final InterpreterFactory interpreterFac = new InterpreterFactory(resolver);

    public static PmmContext createContext(PMMCommandInfo app, String[] args)
	    throws AppXpressException {
	//TODO: remove. this will be in start up...
	DirectoryHelper dHelper = new DirectoryHelper();
	dHelper.ensureAppXpress();
	Set<CLICommandOption> options = app.getOptions();
	CLICommandOptionParser parser = CLICommandOptionParser.createParser(options, args);
	ParsedOptions parsedOptions = parser.parse();
	SimpleShutdown shutdown = new SimpleShutdownImpl();
	CLICommandOptionInterpreter interpreter = interpreterFac.createInterpreter(app, parsedOptions,
		dHelper.getPmProperties());
	Map<CLICommandOption, String> interpretedOptions = interpreter.interpret();
	return new PmmContext(app, shutdown, dHelper.getPmProperties(), interpretedOptions);
    }

}
