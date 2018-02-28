package com.gtnexus.pmm.pmdocgen.cli;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;

import com.gtnexus.pmm.AppXpressDirResolver;
import com.gtnexus.pmm.AppXpressException;
import com.gtnexus.pmm.cli.Select;
import com.gtnexus.pmm.cli.option.CLICommandOptionInterpreter;
import com.gtnexus.pmm.cli.option.CLICommandOptionInterpreterImpl;
import com.gtnexus.pmm.cli.option.ParsedOptions;
import com.gtnexus.pmm.commons.command.PMMCommandInfo;
import com.gtnexus.pmm.commons.file.filter.FileFilterFactory;
import com.gtnexus.pmm.commons.properties.PMProperties;
import com.gtnexus.pmm.pmdocgen.PlatformModuleDocumentGenerator;

public class DocumentGeneratorOptionInterpreter extends CLICommandOptionInterpreterImpl
	implements CLICommandOptionInterpreter {

    private final AppXpressDirResolver resolver;
    private final Select<File> selector;

    public DocumentGeneratorOptionInterpreter(PMMCommandInfo app, 
	    ParsedOptions parsedOptions, PMProperties properties, Select<File> selector,
	    AppXpressDirResolver resolver) {
	super(app, parsedOptions, properties);
	this.resolver = resolver;
	this.selector = selector;
    }

    @Override
    protected ParsedOptions performCustomInterpretation(ParsedOptions parsedOpts)
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

    private boolean isCandidateForArgInjection(ParsedOptions parsedOpts, Path cwd)
	    throws AppXpressException {
	return !parsedOpts.hasOption(PlatformModuleDocumentGenerator.customerOpt)
		&& isCustomerFolder(cwd, PlatformModuleDocumentGenerator.localDirOpt);
    }

    private void doSelect(ParsedOptions parsedOpts, Path cwd) throws AppXpressException {
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
