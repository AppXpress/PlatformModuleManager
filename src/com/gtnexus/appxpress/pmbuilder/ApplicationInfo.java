package com.gtnexus.appxpress.pmbuilder;

import com.gtnexus.appxpress.cli.option.AppXpressOption;

public interface ApplicationInfo {
	
	public String getAppName();

	public String getHelpHeader();
	
	public String getHelpFooter();
	
	public <M extends Enum<M> & AppXpressOption> Class<M> getContextType();
	
}
