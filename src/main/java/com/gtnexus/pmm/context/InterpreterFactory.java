package com.gtnexus.pmm.context;

import java.io.File;

import com.gtnexus.pmm.AppXpressDirResolver;
import com.gtnexus.pmm.cli.FileSelector;
import com.gtnexus.pmm.cli.Select;
import com.gtnexus.pmm.cli.option.CLICommandOptionInterpreter;
import com.gtnexus.pmm.cli.option.ParsedOptions;
import com.gtnexus.pmm.commons.command.PMMCommandInfo;
import com.gtnexus.pmm.commons.properties.PMProperties;
import com.gtnexus.pmm.pmbuilder.cli.BuilderOption;
import com.gtnexus.pmm.pmbuilder.cli.BuilderOptionInterpreter;
import com.gtnexus.pmm.pmbuilder.exception.PMBuilderException;
import com.gtnexus.pmm.pmdocgen.cli.DocumentGeneratorOptionInterpreter;
import com.gtnexus.pmm.pmdocgen.cli.option.DocumentGeneratorOptions;
import com.gtnexus.pmm.pmextractor.cli.ExtractorOption;
import com.gtnexus.pmm.pmextractor.cli.ExtractorOptionInterpreter;

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
	    return new BuilderOptionInterpreter(app, options, properties, selector, resolver);
	} else if (contextType.equals(ExtractorOption.class)) {
	    return new ExtractorOptionInterpreter(app, options, properties, selector, resolver);
	} else if (contextType.equals(DocumentGeneratorOptions.class)) {
	    return new DocumentGeneratorOptionInterpreter(app,
		     options, properties, selector, resolver);
	}
	throw new IllegalArgumentException("Unsupported context type.");
    }

}
