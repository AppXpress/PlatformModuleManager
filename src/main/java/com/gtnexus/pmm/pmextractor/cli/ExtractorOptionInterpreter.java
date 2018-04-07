package com.gtnexus.pmm.pmextractor.cli;

import static com.gtnexus.pmm.AppXpressConstants.ZIP_EXTENSION;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;

import com.gtnexus.pmm.AppXpressDirResolver;
import com.gtnexus.pmm.AppXpressException;
import com.gtnexus.pmm.api.v100.command.PMMCommandInfo;
import com.gtnexus.pmm.cli.Select;
import com.gtnexus.pmm.cli.option.CommandOptionInterpreter;
import com.gtnexus.pmm.cli.option.CommandOptionInterpreterImpl;
import com.gtnexus.pmm.cli.option.ParsedOptions;
import com.gtnexus.pmm.commons.file.filter.FileFilterFactory;
import com.gtnexus.pmm.commons.properties.PMProperties;
import com.gtnexus.pmm.pmextractor.exception.PMExtractorException;

public class ExtractorOptionInterpreter extends CommandOptionInterpreterImpl
	implements CommandOptionInterpreter {

    private final Select<File> selector;
    private AppXpressDirResolver resolver;

    public ExtractorOptionInterpreter(PMMCommandInfo app, 
	    ParsedOptions parsedOptions, PMProperties properties, Select<File> selector,
	    AppXpressDirResolver resolver) {
	super(app, parsedOptions, properties);
	this.selector = selector;
	this.resolver = resolver;
    }

    @Override
    public ParsedOptions performCustomInterpretation(ParsedOptions parsedOpts)
	    throws AppXpressException {
	Path cwd = resolver.resovleCurrentDirectory();
	if (parsedOpts.hasOption(ExtractorOption.SELECT)) {
	    Collection<File> choices = getCandidates(cwd);
	    if (choices.isEmpty()) {
		throw new PMExtractorException("There are no .zip's to select from!");
	    }
	    File selection = selector.select(choices);
	    parsedOpts.put(ExtractorOption.PLATFORM_ZIP, selection);
	}
	if (isCandidateForArgInjection(parsedOpts, cwd)) {
	    parsedOpts.put(ExtractorOption.CUSTOMER, cwd);
	}
	return parsedOpts;
    }

    private boolean isCandidateForArgInjection(ParsedOptions parsedOpts, Path cwd)
	    throws AppXpressException {
	return !parsedOpts.hasOption(ExtractorOption.CUSTOMER) && isCustomerFolder(cwd, ExtractorOption.LOCAL_DIR);
    }

    private Collection<File> getCandidates(Path dir) {
	File[] files = dir.toFile().listFiles(FileFilterFactory.endsWith(ZIP_EXTENSION));
	return Arrays.asList(files);
    }
}
