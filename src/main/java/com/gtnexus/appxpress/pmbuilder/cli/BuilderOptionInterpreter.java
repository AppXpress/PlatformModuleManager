package com.gtnexus.appxpress.pmbuilder.cli;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;

import com.gtnexus.appxpress.AppXpressDirResolver;
import com.gtnexus.appxpress.Exception.AppXpressException;
import com.gtnexus.appxpress.cli.option.CLICommandOptionInterpreter;
import com.gtnexus.appxpress.cli.option.CLIOptionInterpreter;
import com.gtnexus.appxpress.cli.option.ParsedOptions;
import com.gtnexus.appxpress.commons.command.PMMCommandInfo;
import com.gtnexus.appxpress.commons.file.filter.FileFilterFactory;
import com.gtnexus.appxpress.commons.properties.PMProperties;
import com.gtnexus.appxpress.commons.runtime.SimpleShutdown;
import com.gtnexus.appxpress.pmbuilder.Select;
import com.gtnexus.appxpress.pmbuilder.exception.PMBuilderException;

/**
 * 
 * @author jdonovan
 *
 */
public class BuilderOptionInterpreter extends
		CLICommandOptionInterpreter<BuilderOption> implements
		CLIOptionInterpreter<BuilderOption> {

	private final Select<File> selector;
	private final AppXpressDirResolver resolver;

	public BuilderOptionInterpreter(PMMCommandInfo app,
			SimpleShutdown shutdown,
			ParsedOptions<BuilderOption> parsedOptions,
			PMProperties properties, Select<File> selector,
			AppXpressDirResolver resolver) {
		super(app, shutdown, parsedOptions, properties);
		this.selector = selector;
		this.resolver = resolver;
	}

	@Override
	public ParsedOptions<BuilderOption> performCustomInterpretation(
			ParsedOptions<BuilderOption> parsedOpts) throws AppXpressException {
		if (parsedOpts == null) {
			throw new NullPointerException("parsedOpts cannot be null");
		}
		Path cwd = resolver.resovleCurrentDirectory();
		if (parsedOpts.hasOption(BuilderOption.SELECT)) {
			doSelect(parsedOpts, cwd);
		}
		if (isCandidateForArgInjection(parsedOpts, cwd)) {
			parsedOpts.put(BuilderOption.CUSTOMER, cwd);
		}//TODO: can we inject platform as well?
		return parsedOpts;
	}

	private void doSelect(ParsedOptions<BuilderOption> parsedOpts, Path cwd)
			throws AppXpressException, PMBuilderException {
		if (!isCustomerFolder(cwd, BuilderOption.LOCAL_DIR)) {
			throw new PMBuilderException(
					"The select option must be run from a cutomer folder.");
		}
		Collection<File> choices = getCandidates(cwd);
		if (choices.isEmpty()) {
			throw new PMBuilderException("Nothing to select from!");
		}
		File selection = selector.select(choices);
		parsedOpts.put(BuilderOption.MODULE, selection);
		parsedOpts.put(BuilderOption.CUSTOMER, cwd);
	}

	private boolean isCandidateForArgInjection(
			ParsedOptions<BuilderOption> parsedOpts, Path cwd)
			throws AppXpressException {
		return !parsedOpts.hasOption(BuilderOption.CUSTOMER)
				&& isCustomerFolder(cwd, BuilderOption.LOCAL_DIR);
	}

	private Collection<File> getCandidates(Path dir) {
		File[] files = dir.toFile().listFiles(
				FileFilterFactory.directoriesOnly());
		return Arrays.asList(files);
	}

}
