package com.gtnexus.appxpress.context;

import java.io.File;

import com.gtnexus.appxpress.cli.option.AppXpressOption;
import com.gtnexus.appxpress.cli.option.BuilderOptionInterpreter;
import com.gtnexus.appxpress.cli.option.CLIOptionInterpreter;
import com.gtnexus.appxpress.cli.option.ExtractorOptionInterpreter;
import com.gtnexus.appxpress.cli.option.ParsedOptions;
import com.gtnexus.appxpress.commons.AppXpressDirResolver;
import com.gtnexus.appxpress.commons.CommandInformation;
import com.gtnexus.appxpress.commons.PMProperties;
import com.gtnexus.appxpress.commons.SimpleShutdown;
import com.gtnexus.appxpress.pmbuilder.PlatformSelector;
import com.gtnexus.appxpress.pmbuilder.Select;
import com.gtnexus.appxpress.pmbuilder.cli.BuilderOption;
import com.gtnexus.appxpress.pmbuilder.exception.PMBuilderException;
import com.gtnexus.appxpress.pmextractor.cli.ExtractorOption;

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
	public <T extends AppXpressOption> CLIOptionInterpreter<T> createInterpreter(
			CommandInformation app, SimpleShutdown shutdown,
			ParsedOptions options, PMProperties properties)
			throws PMBuilderException {
		Class<T> contextType = app.getContextType();
		Select<File> selector = new PlatformSelector(System.in, System.out);
		CLIOptionInterpreter<T> interp = null;
		if (contextType.equals(BuilderOption.class)) {
			interp = (CLIOptionInterpreter<T>) new BuilderOptionInterpreter(app,
					shutdown, (ParsedOptions) options,
					properties, selector, resolver);
		} else if(contextType.equals(ExtractorOption.class)) {
			interp =  (CLIOptionInterpreter<T>) new ExtractorOptionInterpreter(app,
					shutdown, (ParsedOptions) options,
					properties, selector, resolver);
		} else {
			throw new IllegalArgumentException("Unsupported context type.");	
		}
		return interp;
	}

}
