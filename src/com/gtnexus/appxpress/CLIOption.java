package com.gtnexus.appxpress;


public interface CLIOption {

	
	public String toString();
    public String getName();
    public Class<?> getType();
    public String getDescription();
    public boolean hasArg();
    
}
