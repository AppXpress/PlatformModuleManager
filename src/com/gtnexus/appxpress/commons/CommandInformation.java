package com.gtnexus.appxpress.commons;

import com.gtnexus.appxpress.cli.option.AppXpressOption;

public interface CommandInformation {
	
	public String getCommandName();

	public String getHelpHeader();
	
	public String getHelpFooter();
	
	public <M extends AppXpressOption> Class<M> getContextType();
	
}
