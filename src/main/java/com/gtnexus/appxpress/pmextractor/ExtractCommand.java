package com.gtnexus.appxpress.pmextractor;

import com.google.common.base.Preconditions;
import com.gtnexus.appxpress.commons.command.Command;
import com.gtnexus.appxpress.context.ContextFactory;
import com.gtnexus.appxpress.context.PmmContext;
import com.gtnexus.appxpress.exception.AppXpressException;
import com.gtnexus.appxpress.pmextractor.cli.ExtractorOption;

public class ExtractCommand implements Command {

    private String[] args;
    private ContextFactory contextFactory;

    public ExtractCommand(String... args) {
	Preconditions.checkNotNull(args);
	this.args = args;
	this.contextFactory = new ContextFactory();
    }

    @Override
    public void execute() throws AppXpressException {
	PlatformModuleExtractor extractor = new PlatformModuleExtractor();
	PmmContext<ExtractorOption> context;
	try {
	    context = contextFactory.createContext(extractor, args);
	    extractor.extract(context);
	} catch (AppXpressException e) {
	    throw new AppXpressException("extract has failed.", e);
	}
    }

}
