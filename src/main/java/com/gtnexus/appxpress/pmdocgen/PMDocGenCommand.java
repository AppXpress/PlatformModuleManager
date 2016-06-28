package com.gtnexus.appxpress.pmdocgen;

import com.google.common.base.Preconditions;
import com.gtnexus.appxpress.cli.option.CLICommandOption;
import com.gtnexus.appxpress.commons.command.Command;
import com.gtnexus.appxpress.context.AppXpressContext;
import com.gtnexus.appxpress.context.ContextFactory;
import com.gtnexus.appxpress.exception.AppXpressException;

public class PMDocGenCommand implements Command {

	private String[] args;
	private ContextFactory contextFactory;
	
	public PMDocGenCommand(String ...args) {
		Preconditions.checkNotNull(args);
		this.args = args;
		this.contextFactory = new ContextFactory();
	}

	@Override
	public void execute() throws AppXpressException {
		PlatformModuleDocumentGenerator pmdg = new PlatformModuleDocumentGenerator();
		AppXpressContext<CLICommandOption> context;
		try {
			context = contextFactory.createContext(pmdg,args);
			pmdg.generateDocs(context);
		} catch (AppXpressException e) {
			throw new AppXpressException("docgen has failed.", e);
		}
	}

}
