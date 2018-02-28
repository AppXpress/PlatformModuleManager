package com.gtnexus.pmm.pmbuilder.cli;

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
import com.gtnexus.pmm.pmbuilder.exception.PMBuilderException;

/**
 * 
 * @author jdonovan
 *
 */
public class BuilderOptionInterpreter extends CLICommandOptionInterpreterImpl
	implements CLICommandOptionInterpreter {

    private final Select<File> selector;
    private final AppXpressDirResolver resolver;

    public BuilderOptionInterpreter(
	    PMMCommandInfo app,
	    ParsedOptions parsedOptions,
	    PMProperties properties,
	    Select<File> selector,
	    AppXpressDirResolver resolver) {
	super(app, parsedOptions, properties);
	this.selector = selector;
	this.resolver = resolver;
    }

    @Override
    public ParsedOptions performCustomInterpretation(ParsedOptions parsedOpts)
	    throws AppXpressException {
	if (parsedOpts == null) {
	    throw new NullPointerException("parsedOpts cannot be null");
	}
	Path cwd = resolver.resovleCurrentDirectory();
	if (parsedOpts.hasOption(BuilderOption.SELECT)) {
	    doSelect(parsedOpts, cwd);
	}
	if (isCandidateForArgInjection(parsedOpts, cwd)) {
	    parsedOpts.put(BuilderOption.CUSTOMER, cwd);
	}
	return parsedOpts;
    }

    private void doSelect(ParsedOptions parsedOpts, Path cwd) throws AppXpressException {
	if (!isCustomerFolder(cwd, BuilderOption.LOCAL_DIR)) {
	    throw new PMBuilderException("The select option must be run from a customer folder.");
	}
	Collection<File> choices = getCandidates(cwd);
	if (choices.isEmpty()) {
	    throw new PMBuilderException("Nothing to select from!");
	}
	File selection = selector.select(choices);
	parsedOpts.put(BuilderOption.MODULE, selection);
	parsedOpts.put(BuilderOption.CUSTOMER, cwd);
    }

    private boolean isCandidateForArgInjection(ParsedOptions parsedOpts, Path cwd)
	    throws AppXpressException {
	return !parsedOpts.hasOption(BuilderOption.CUSTOMER) && isCustomerFolder(cwd, BuilderOption.LOCAL_DIR);
    }

    private Collection<File> getCandidates(Path dir) {
	File[] files = dir.toFile().listFiles(FileFilterFactory.directoriesOnly());
	return Arrays.asList(files);
    }

}
