package com.gtnexus.appxpress.context;

import java.io.File;

import com.gtnexus.appxpress.AppXpressDirResolver;
import com.gtnexus.appxpress.cli.FileSelector;
import com.gtnexus.appxpress.cli.Select;
import com.gtnexus.appxpress.cli.option.CLICommandOptionInterpreter;
import com.gtnexus.appxpress.cli.option.ParsedOptions;
import com.gtnexus.appxpress.commons.command.PMMCommandInfo;
import com.gtnexus.appxpress.commons.properties.PMProperties;
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
    public CLICommandOptionInterpreter createInterpreter(PMMCommandInfo app, ParsedOptions options, PMProperties properties)
	    throws PMBuilderException {
	Class<?> contextType = app.getContextType();
	Select<File> selector = new FileSelector(System.in, System.out);
	if (contextType.equals(BuilderOption.class)) {
	    // TODO: we need to remove the unchecked conversion
	    return new BuilderOptionInterpreter(app, options, properties, selector, resolver);
	} else if (contextType.equals(ExtractorOption.class)) {
	    // TODO: we need to remove the unchecked conversion
	    return new ExtractorOptionInterpreter(app, options, properties, selector, resolver);
	} else if (contextType.equals(DocumentGeneratorOptions.class)) {
	    return new DocumentGeneratorOptionInterpreter(app,
		     options, properties, selector, resolver);
	}
	throw new IllegalArgumentException("Unsupported context type.");
    }

}
