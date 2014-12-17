package com.gtnexus.appxpress.cli.option;

import java.nio.file.Path;

import com.gtnexus.appxpress.AppXpressDirResolver;
import com.gtnexus.appxpress.AppXpressException;
import com.gtnexus.appxpress.PMProperties;
import com.gtnexus.appxpress.context.AppXpressContext;
import com.gtnexus.appxpress.pmbuilder.PlatformSelector;
import com.gtnexus.appxpress.pmbuilder.cli.BuilderOption;
import com.gtnexus.appxpress.pmbuilder.exception.PMBuilderException;

public class BuilderOptionInterpreter extends
		AppXpressOptionInterpreter<BuilderOption> implements CLIOptionInterpreter<BuilderOption>{

	private final AppXpressDirResolver resolver;
	private AppXpressContext<BuilderOption> context;

	public BuilderOptionInterpreter(ParsedOptions<BuilderOption> parsedOptions) {
		super(parsedOptions);
		this.resolver = new AppXpressDirResolver();
	}

	private String select() throws PMBuilderException {
		Path cwd = resolver.resovleCurrentDirectory();
		PlatformSelector selector = PlatformSelector.createPlatformSelector(cwd
				.toFile());
		return selector.select();
	}

	@Override
	public ParsedOptions<BuilderOption> customInterpretation(
			ParsedOptions<BuilderOption> parsedOpts) throws AppXpressException {
		if (parsedOpts.hasOption(BuilderOption.SELECT_PlATFORM)) {
			PMProperties props = context.getPMProperties();
			String selection = select();
		}
		return parsedOpts;
	}
}
