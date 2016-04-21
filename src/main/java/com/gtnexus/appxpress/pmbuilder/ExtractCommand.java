package com.gtnexus.appxpress.pmbuilder;

import com.google.common.base.Preconditions;
import com.gtnexus.appxpress.Exception.AppXpressException;
import com.gtnexus.appxpress.commons.command.Command;
import com.gtnexus.appxpress.context.AppXpressContext;
import com.gtnexus.appxpress.context.ContextFactory;
import com.gtnexus.appxpress.pmextractor.PlatformModuleExtractor;
import com.gtnexus.appxpress.pmextractor.cli.ExtractorOption;

public class ExtractCommand implements Command {

	private String[] args;
	private ContextFactory contextFactory;
	
	public ExtractCommand(String ...args) {
		Preconditions.checkNotNull(args);
		this.args = args;
		this.contextFactory = new ContextFactory();
	}
	
	@Override
	public void execute() {
		PlatformModuleExtractor extractor = new PlatformModuleExtractor();
		AppXpressContext<ExtractorOption> context;
		try {
			context = contextFactory.createContext(extractor, args);
			extractor.extract(context);
		} catch (AppXpressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
