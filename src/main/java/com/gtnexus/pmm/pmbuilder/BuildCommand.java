package com.gtnexus.pmm.pmbuilder;

import com.google.common.base.Preconditions;
import com.gtnexus.pmm.AppXpressException;
import com.gtnexus.pmm.commons.command.Command;
import com.gtnexus.pmm.context.ContextFactory;
import com.gtnexus.pmm.context.PmmContext;

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
