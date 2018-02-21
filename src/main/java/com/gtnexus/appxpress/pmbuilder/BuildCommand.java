package com.gtnexus.appxpress.pmbuilder;

import com.google.common.base.Preconditions;
import com.gtnexus.appxpress.commons.command.Command;
import com.gtnexus.appxpress.context.AppXpressContext;
import com.gtnexus.appxpress.context.ContextFactory;
import com.gtnexus.appxpress.exception.AppXpressException;
import com.gtnexus.appxpress.pmbuilder.cli.BuilderOption;

public class BuildCommand implements Command {

    private String[] args;
    private ContextFactory contextFactory;

    public BuildCommand(String... args) {
	Preconditions.checkNotNull(args);
	this.args = args;
	this.contextFactory = new ContextFactory();
    }

    @Override
    public void execute() throws AppXpressException {
	PlatformModuleBuilder pmb = new PlatformModuleBuilder();
	AppXpressContext<BuilderOption> context;
	try {
	    context = contextFactory.createContext(pmb, args);
	    pmb.build(context);
	} catch (AppXpressException e) {
	    throw new AppXpressException("build has failed.", e);
	}
    }

}
