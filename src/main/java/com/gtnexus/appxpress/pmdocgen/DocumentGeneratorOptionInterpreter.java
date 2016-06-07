package com.gtnexus.appxpress.pmdocgen;

import java.nio.file.Path;

import com.gtnexus.appxpress.AppXpressDirResolver;
import com.gtnexus.appxpress.cli.option.CLICommandOption;
import com.gtnexus.appxpress.cli.option.CLICommandOptionInterpreter;
import com.gtnexus.appxpress.cli.option.CLIOptionInterpreter;
import com.gtnexus.appxpress.cli.option.ParsedOptions;
import com.gtnexus.appxpress.commons.command.PMMCommandInfo;
import com.gtnexus.appxpress.commons.properties.PMProperties;
import com.gtnexus.appxpress.commons.runtime.SimpleShutdown;
import com.gtnexus.appxpress.exception.AppXpressException;

public class DocumentGeneratorOptionInterpreter extends CLICommandOptionInterpreter<CLICommandOption>
		implements CLIOptionInterpreter<CLICommandOption> {

	private final AppXpressDirResolver resolver;
	
	public DocumentGeneratorOptionInterpreter(PMMCommandInfo app, SimpleShutdown shutdown,
			ParsedOptions<CLICommandOption> parsedOptions, PMProperties properties, AppXpressDirResolver resolver) {
		super(app, shutdown, parsedOptions, properties);
		this.resolver = resolver;
	}

	@Override
	protected ParsedOptions<CLICommandOption> performCustomInterpretation(ParsedOptions<CLICommandOption> parsedOpts) throws AppXpressException {
		Path cwd = resolver.resovleCurrentDirectory();
		if(isCandidateForArgInjection(parsedOpts, cwd)) {
			parsedOpts.put(PlatformModuleDocumentGenerator.customerOpt, cwd);
		}
		return parsedOpts;
	}
	
	private boolean isCandidateForArgInjection(
			ParsedOptions<CLICommandOption> parsedOpts, Path cwd)
			throws AppXpressException {
		return !parsedOpts.hasOption(PlatformModuleDocumentGenerator.customerOpt)
				&& isCustomerFolder(cwd, PlatformModuleDocumentGenerator.localDirOpt);
	}

}
