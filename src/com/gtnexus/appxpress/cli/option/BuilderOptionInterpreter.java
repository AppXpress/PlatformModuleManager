package com.gtnexus.appxpress.cli.option;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
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
	private AppXpressDirResolver resolver;

	public BuilderOptionInterpreter(ApplicationInfo app,
			SimpleShutdown shutdown,
			ParsedOptions<BuilderOption> parsedOptions,
			PMProperties properties, Select<File> selector, AppXpressDirResolver resolver) {
		super(app, shutdown, parsedOptions, properties);
		this.selector = selector;
		this.resolver = resolver;
	}

	@Override
	public ParsedOptions<BuilderOption> customInterpretation(
			ParsedOptions<BuilderOption> parsedOpts) throws AppXpressException {
		if (parsedOpts == null) {
			throw new NullPointerException("parsedOpts cannot be null");
		}
		if (parsedOpts.hasOption(BuilderOption.SELECT_PlATFORM)) {
			Path cwd = resolver.resovleCurrentDirectory();
			Collection<File> choices = ensureCriteriaIsMet(cwd);
			File selection = selector.select(choices);
			parsedOpts.put(BuilderOption.PLATFORM, selection.getName());
			parsedOpts.put(BuilderOption.CUSTOMER, cwd.getFileName().toString());
		}
		return parsedOpts;
	}

	
	//TODO some ugly ass code.
	public Collection<File> ensureCriteriaIsMet(Path dir) throws PMBuilderException {
		final String localDir = properties.getProperty(BuilderOption.LOCAL_DIR
				.getLongName());
		if (localDir == null || localDir.isEmpty()) {
			throw new PMBuilderException(
					"Local Directory property is not set. "
							+ "Please check your AppXpress properties file"
							+ "before trying to run the Select option again.");
		}
		Path cwdP = dir.getParent();
		Path ld = Paths.get(localDir);
		if(!cwdP.equals(ld)) {
			throw new PMBuilderException("The select option must be run from a cutomer folder.");
		}
		File[] files = dir.toFile().listFiles(FileFilterFactory.directoriesOnly());
		if(files.length == 0) {
			throw new PMBuilderException("Nothing to select from!");
		}
		return Arrays.asList(files);
	}
	
}
