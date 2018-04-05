package com.gtnexus.pmm.context;

public class ContextFactory {
    
    private ContextFactory() {}

//    private static final AppXpressDirResolver resolver = new AppXpressDirResolver();
//    private static final InterpreterFactory interpreterFac = new InterpreterFactory(resolver);

//    public static PmmContext createContext(PMMCommandInfo app, String[] args)
//	    throws AppXpressException {
//	//TODO: remove. this will be in start up...
//	DirectoryHelper dHelper = new DirectoryHelper();
//	dHelper.ensureAppXpress();
//	Set<CLICommandOption> options = app.getOptions();
//	CLICommandOptionParser parser = CLICommandOptionParser.createParser(options, args);
//	ParsedOptions parsedOptions = parser.parse();
//	SimpleShutdown shutdown = new SimpleShutdownImpl();
//	CLICommandOptionInterpreter interpreter = interpreterFac.createInterpreter(app, parsedOptions,
//		dHelper.getPmProperties());
//	Map<CLICommandOption, String> interpretedOptions = interpreter.interpret();
//	return new PmmContext(app, shutdown, dHelper.getPmProperties(), interpretedOptions);
//    }

}
