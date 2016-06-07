package com.gtnexus.appxpress.pmextractor.cli;

import static com.gtnexus.appxpress.AppXpressConstants.ZIP_EXTENSION;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;

import com.gtnexus.appxpress.AppXpressDirResolver;
import com.gtnexus.appxpress.cli.option.CLICommandOptionInterpreter;
import com.gtnexus.appxpress.cli.option.CLIOptionInterpreter;
import com.gtnexus.appxpress.cli.option.ParsedOptions;
import com.gtnexus.appxpress.commons.command.PMMCommandInfo;
import com.gtnexus.appxpress.commons.file.filter.FileFilterFactory;
import com.gtnexus.appxpress.commons.properties.PMProperties;
import com.gtnexus.appxpress.commons.runtime.SimpleShutdown;
import com.gtnexus.appxpress.exception.AppXpressException;
import com.gtnexus.appxpress.pmbuilder.Select;
import com.gtnexus.appxpress.pmextractor.exception.PMExtractorException;

public class ExtractorOptionInterpreter extends
		CLICommandOptionInterpreter<ExtractorOption> implements
		CLIOptionInterpreter<ExtractorOption> {

	private final Select<File> selector;
	private AppXpressDirResolver resolver;
	
	public ExtractorOptionInterpreter(PMMCommandInfo app,
			SimpleShutdown shutdown,
			ParsedOptions<ExtractorOption> parsedOptions,
			PMProperties properties, Select<File> selector,
			AppXpressDirResolver resolver) {
		super(app, shutdown, parsedOptions, properties);
		this.selector = selector;
		this.resolver = resolver;
	}

	@Override
	public ParsedOptions<ExtractorOption> performCustomInterpretation(
			ParsedOptions<ExtractorOption> parsedOpts)
			throws AppXpressException {
		Path cwd = resolver.resovleCurrentDirectory();
		if (parsedOpts.hasOption(ExtractorOption.SELECT)) {
			Collection<File> choices = getCandidates(cwd);
			if(choices.isEmpty()) {
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

	private boolean isCandidateForArgInjection(
			ParsedOptions<ExtractorOption> parsedOpts, Path cwd)
			throws AppXpressException {
		return !parsedOpts.hasOption(ExtractorOption.CUSTOMER)
				&& isCustomerFolder(cwd, ExtractorOption.LOCAL_DIR);
	}
	
	private Collection<File> getCandidates(Path dir) {
		File[] files = dir.toFile().listFiles(
				FileFilterFactory.endsWith(ZIP_EXTENSION));
		return Arrays.asList(files);
	}
}
