package com.gtnexus.appxpress.cli.option;

import static com.gtnexus.appxpress.AppXpressConstants.ZIP_EXTENSION;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;

import com.gtnexus.appxpress.AppXpressDirResolver;
import com.gtnexus.appxpress.AppXpressException;
import com.gtnexus.appxpress.commons.ApplicationInfo;
import com.gtnexus.appxpress.commons.PMProperties;
import com.gtnexus.appxpress.commons.SimpleShutdown;
import com.gtnexus.appxpress.commons.file.filter.FileFilterFactory;
import com.gtnexus.appxpress.pmbuilder.Select;
import com.gtnexus.appxpress.pmbuilder.cli.BuilderOption;
import com.gtnexus.appxpress.pmbuilder.exception.PMBuilderException;

/**
 * 
 * @author jdonovan
 *
 */
public class BuilderOptionInterpreter extends
		AppXpressOptionInterpreter<BuilderOption> implements
		CLIOptionInterpreter<BuilderOption> {

	private final Select<File> selector;
	private final AppXpressDirResolver resolver;

	public BuilderOptionInterpreter(ApplicationInfo app,
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
			if (!isCustomerFolder(cwd, BuilderOption.LOCAL_DIR)) {
				throw new PMBuilderException(
						"The select option must be run from a cutomer folder.");
			}
			Collection<File> choices = getCandidates(cwd);
			if (choices.isEmpty()) {
				throw new PMBuilderException("Nothing to select from!");
			}
			File selection = selector.select(choices);
			parsedOpts.put(BuilderOption.PLATFORM, selection);
			parsedOpts.put(BuilderOption.CUSTOMER, cwd);
		}
		if (isCandidateForArgInjection(parsedOpts, cwd)) {
			parsedOpts.put(BuilderOption.CUSTOMER, cwd);
		}//TODO: can we inject platform as well?
		return parsedOpts;
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
