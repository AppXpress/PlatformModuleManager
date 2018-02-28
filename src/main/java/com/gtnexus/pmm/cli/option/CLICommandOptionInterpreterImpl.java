package com.gtnexus.pmm.cli.option;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import org.apache.commons.cli.HelpFormatter;

import com.gtnexus.pmm.AppXpressException;
import com.gtnexus.pmm.cli.CLIOptsAndPropConsolidator;
import com.gtnexus.pmm.commons.command.PMMCommandInfo;
import com.gtnexus.pmm.commons.properties.PMProperties;

public abstract class CLICommandOptionInterpreterImpl implements CLICommandOptionInterpreter {

    private ParsedOptions parsedOptions;
    private final PMMCommandInfo app;
    // protected final SimpleShutdown shutdown;
    protected final PMProperties properties;

    public CLICommandOptionInterpreterImpl(PMMCommandInfo app, /* SimpleShutdown shutdown, */ ParsedOptions parsedOptions,
	    PMProperties properties) {
	this.app = app;
	// this.shutdown = shutdown;
	this.parsedOptions = parsedOptions;
	this.properties = properties;
    }

    @Override
    public final Map<CLICommandOption, String> interpret() throws AppXpressException {
	// code smell: why is the interpreter trying to exit? individual sub commands
	// should control that.
	if (parsedOptions.isHelpFlagSet()) {
	    HelpFormatter helpFormatter = new HelpFormatter();
	    helpFormatter.printHelp(app.getName(), app.getHelpHeader(), parsedOptions.getOptions(),
		    app.getHelpFooter());
	    // shutdown.shutdown();
	    throw new RuntimeException("Was supposed to shut down...");
	}
	parsedOptions = performCustomInterpretation(parsedOptions);
	CLIOptsAndPropConsolidator consolidator = new CLIOptsAndPropConsolidator(parsedOptions.getOptionsMap(),
		parsedOptions.getCliOptionSet(), properties);
	Map<CLICommandOption, String> optMap = consolidator.consolidate();
	return optMap;
    }

    protected boolean isCustomerFolder(Path dir, CLICommandOption localDirKey) throws AppXpressException {
	final String localDir = resolveLocalDir(localDirKey);
	if (localDir == null || localDir.isEmpty()) {
	    throw new AppXpressException("Local Directory property is not set. "
		    + "Please check your AppXpress properties file" + "before trying to run the Select option again.");
	}
	Path parent = dir.getParent();
	Path ld = Paths.get(localDir);
	if (parent.equals(ld)) {
	    return true;
	}
	return false;
    }

    private String resolveLocalDir(CLICommandOption localDirKey) {
	if (parsedOptions.hasOption(localDirKey)) {
	    return parsedOptions.getOption(localDirKey);
	}
	return properties.getProperty(localDirKey);
    }

    protected abstract ParsedOptions performCustomInterpretation(ParsedOptions parsedOpts) throws AppXpressException;

}
