package com.gtnexus.appxpress.context;

import java.io.File;

import com.gtnexus.appxpress.AppXpressDirResolver;
import com.gtnexus.appxpress.cli.option.CLICommandOption;
import com.gtnexus.appxpress.cli.option.CLIOptionInterpreter;
import com.gtnexus.appxpress.cli.option.ParsedOptions;
import com.gtnexus.appxpress.commons.command.PMMCommandInfo;
import com.gtnexus.appxpress.commons.properties.PMProperties;
import com.gtnexus.appxpress.commons.runtime.SimpleShutdown;
import com.gtnexus.appxpress.pmbuilder.PlatformSelector;
import com.gtnexus.appxpress.pmbuilder.Select;
import com.gtnexus.appxpress.pmbuilder.cli.BuilderOption;
import com.gtnexus.appxpress.pmbuilder.cli.BuilderOptionInterpreter;
import com.gtnexus.appxpress.pmbuilder.exception.PMBuilderException;
import com.gtnexus.appxpress.pmdocgen.cli.DocumentGeneratorOptionInterpreter;
import com.gtnexus.appxpress.pmdocgen.cli.option.DocumentGeneratorOptions;
import com.gtnexus.appxpress.pmextractor.cli.ExtractorOption;
import com.gtnexus.appxpress.pmextractor.cli.ExtractorOptionInterpreter;

/**
 * 
 * @author jdonovan
 *
 */
public class InterpreterFactory {

	private final AppXpressDirResolver resolver;

	public InterpreterFactory(AppXpressDirResolver resolver) {
		this.resolver = resolver;
	}

	/**
	 * 
	 * @param contextType
	 * @param optionss
	 * @throws IllegalArgumentException
	 *             if contextType is not supported.
	 * @return
	 * @throws PMBuilderException
	 */
	@SuppressWarnings("unchecked")
	public <T extends CLICommandOption> CLIOptionInterpreter<T> createInterpreter(
			PMMCommandInfo app, SimpleShutdown shutdown,
			ParsedOptions<T> options, PMProperties properties)
			throws PMBuilderException {
		Class<?> contextType = app.getContextType();
		Select<File> selector = new PlatformSelector(System.in, System.out);
		if (contextType.equals(BuilderOption.class)) {
			//TODO: we need to remove the unchecked conversion
			return (CLIOptionInterpreter<T>) new BuilderOptionInterpreter(app,
					shutdown, (ParsedOptions<BuilderOption>) options,
					properties, selector, resolver);
		} else if(contextType.equals(ExtractorOption.class)) {
			//TODO: we need to remove the unchecked conversion
			return (CLIOptionInterpreter<T>) new ExtractorOptionInterpreter(app,
					shutdown, (ParsedOptions<ExtractorOption>) options,
					properties, selector, resolver);
		} else if(contextType.equals(DocumentGeneratorOptions.class)) {
			return (CLIOptionInterpreter<T>) new DocumentGeneratorOptionInterpreter(app,
					shutdown, (ParsedOptions<CLICommandOption>) options,
					properties, selector, resolver);
		}
		throw new IllegalArgumentException("Unsupported context type.");
	}

}
