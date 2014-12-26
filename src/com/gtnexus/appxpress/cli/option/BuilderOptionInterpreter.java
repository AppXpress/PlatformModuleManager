package com.gtnexus.appxpress.cli.option;

import java.nio.file.Path;

import com.gtnexus.appxpress.AppXpressDirResolver;
import com.gtnexus.appxpress.AppXpressException;
import com.gtnexus.appxpress.PMProperties;
import com.gtnexus.appxpress.context.SimpleShutdown;
import com.gtnexus.appxpress.pmbuilder.ApplicationInfo;
import com.gtnexus.appxpress.pmbuilder.PlatformSelector;
import com.gtnexus.appxpress.pmbuilder.PlatformSelectorFactory;
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

	private final AppXpressDirResolver resolver;
	private final PMProperties properties;
	private PlatformSelectorFactory selectorFactory;

	public PlatformSelectorFactory getSelectorFactory() {
		return selectorFactory;
	}

	public void setSelectorFactory(PlatformSelectorFactory selectorFactory) {
		this.selectorFactory = selectorFactory;
	}

	public BuilderOptionInterpreter(ApplicationInfo app, SimpleShutdown shutdown, ParsedOptions<BuilderOption> parsedOptions, PMProperties properties) {
		super(app, shutdown, parsedOptions, properties);
		this.resolver = new AppXpressDirResolver();
		this.properties = properties;
		this.selectorFactory = new PlatformSelectorFactory();
	}

	@Override
	public ParsedOptions<BuilderOption> customInterpretation(
			ParsedOptions<BuilderOption> parsedOpts) throws AppXpressException {
		if (parsedOpts.hasOption(BuilderOption.SELECT_PlATFORM)) {
			//TODO
			String selection = select();
			parsedOpts.put(BuilderOption.PLATFORM, selection);
		}
		return parsedOpts;
	}
	
	private String select() throws PMBuilderException {
		Path cwd = resolver.resovleCurrentDirectory();
		PlatformSelector platformSelector = selectorFactory
				.createPlatformSelector(cwd.toFile(), properties);
		return platformSelector.select();
	}

}
