package com.gtnexus.appxpress.commons;

import java.util.Set;

import com.gtnexus.appxpress.cli.option.AppXpressOption;

public interface ApplicationInfo {
	
	public String getAppName();

	public String getHelpHeader();
	
	public String getHelpFooter();
	
	public Class<?> getContextType();
	
	public <M extends AppXpressOption> Set<M> getOptions();
	
}
