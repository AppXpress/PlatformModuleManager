package com.gtnexus.appxpress.commons.command;

import java.util.Set;

import com.gtnexus.appxpress.cli.option.CLICommandOption;

public interface PMMCommandInfo {
	
	public String getName();

	public String getHelpHeader();
	
	public String getHelpFooter();
	
	public Class<?> getContextType();
	
	public <M extends CLICommandOption> Set<M> getOptions();
	
}
