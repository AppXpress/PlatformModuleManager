package com.gtnexus.appxpress.pmdocgen.cli;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;

import com.gtnexus.appxpress.AppXpressDirResolver;
import com.gtnexus.appxpress.cli.Select;
import com.gtnexus.appxpress.cli.option.CLICommandOption;
import com.gtnexus.appxpress.cli.option.CLICommandOptionInterpreter;
import com.gtnexus.appxpress.cli.option.CLIOptionInterpreter;
import com.gtnexus.appxpress.cli.option.ParsedOptions;
import com.gtnexus.appxpress.commons.command.PMMCommandInfo;
import com.gtnexus.appxpress.commons.file.filter.FileFilterFactory;
import com.gtnexus.appxpress.commons.properties.PMProperties;
import com.gtnexus.appxpress.commons.runtime.SimpleShutdown;
import com.gtnexus.appxpress.exception.AppXpressException;
import com.gtnexus.appxpress.pmdocgen.PlatformModuleDocumentGenerator;

public class DocumentGeneratorOptionInterpreter extends CLICommandOptionInterpreter<CLICommandOption>
	implements CLIOptionInterpreter<CLICommandOption> {

    private final AppXpressDirResolver resolver;
    private final Select<File> selector;

    public DocumentGeneratorOptionInterpreter(PMMCommandInfo app, SimpleShutdown shutdown,
	    ParsedOptions<CLICommandOption> parsedOptions, PMProperties properties, Select<File> selector,
	    AppXpressDirResolver resolver) {
	super(app, shutdown, parsedOptions, properties);
	this.resolver = resolver;
	this.selector = selector;
    }

    @Override
    protected ParsedOptions<CLICommandOption> performCustomInterpretation(ParsedOptions<CLICommandOption> parsedOpts)
	    throws AppXpressException {
	Path cwd = resolver.resovleCurrentDirectory();
	if (parsedOpts.hasOption(PlatformModuleDocumentGenerator.selectOpt)) {
	    doSelect(parsedOpts, cwd);
	}
	if (isCandidateForArgInjection(parsedOpts, cwd)) {
	    parsedOpts.put(PlatformModuleDocumentGenerator.customerOpt, cwd);
	}
	return parsedOpts;
    }

    private boolean isCandidateForArgInjection(ParsedOptions<CLICommandOption> parsedOpts, Path cwd)
	    throws AppXpressException {
	return !parsedOpts.hasOption(PlatformModuleDocumentGenerator.customerOpt)
		&& isCustomerFolder(cwd, PlatformModuleDocumentGenerator.localDirOpt);
    }

    private void doSelect(ParsedOptions<CLICommandOption> parsedOpts, Path cwd) throws AppXpressException {
	if (!isCustomerFolder(cwd, PlatformModuleDocumentGenerator.localDirOpt)) {
	    throw new AppXpressException("The select option must be run from a customer folder.");
	}
	Collection<File> choices = getCandidates(cwd);
	if (choices.isEmpty()) {
	    throw new AppXpressException("Nothing to select from!");
	}
	File selection = selector.select(choices);
	parsedOpts.put(PlatformModuleDocumentGenerator.moduleOpt, selection);
	parsedOpts.put(PlatformModuleDocumentGenerator.customerOpt, cwd);
    }

    private Collection<File> getCandidates(Path dir) {
	File[] files = dir.toFile().listFiles(FileFilterFactory.directoriesOnly());
	return Arrays.asList(files);
    }

}
