package com.gtnexus.appxpress.pmbuilder;

import com.google.common.base.Preconditions;
import com.gtnexus.appxpress.commons.command.Command;
import com.gtnexus.appxpress.context.ContextFactory;
import com.gtnexus.appxpress.context.PmmContext;
import com.gtnexus.appxpress.exception.AppXpressException;

public class BuildCommand implements Command {

    private String[] args;

    public BuildCommand(String... args) {
	Preconditions.checkNotNull(args);
	this.args = args;
    }

    @Override
    public void execute() throws AppXpressException {
	PlatformModuleBuilder pmb = new PlatformModuleBuilder();
	PmmContext context;
	try {
	    context = ContextFactory.createContext(pmb, args);
	    pmb.build(context);
	} catch (AppXpressException e) {
	    throw new AppXpressException("build has failed.", e);
	}
    }

}
