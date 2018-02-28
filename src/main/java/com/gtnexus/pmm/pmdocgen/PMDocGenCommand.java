package com.gtnexus.pmm.pmdocgen;

import com.google.common.base.Preconditions;
import com.gtnexus.pmm.AppXpressException;
import com.gtnexus.pmm.commons.command.Command;
import com.gtnexus.pmm.context.ContextFactory;
import com.gtnexus.pmm.context.PmmContext;

public class PMDocGenCommand implements Command {

    private String[] args;

    public PMDocGenCommand(String... args) {
	Preconditions.checkNotNull(args);
	this.args = args;
    }

    @Override
    public void execute() throws AppXpressException {
	PlatformModuleDocumentGenerator pmdg = new PlatformModuleDocumentGenerator();
	PmmContext context;
	try {
	    context = ContextFactory.createContext(pmdg, args);
	    pmdg.generateDocs(context);
	} catch (AppXpressException e) {
	    throw new AppXpressException("docgen has failed.", e);
	}
    }

}
