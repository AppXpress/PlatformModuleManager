package com.gtnexus.appxpress.cli;


public interface CLIOption {
	public String toString();
    public String getName();
    public Class<?> getType();
    public String getDescription();
    public boolean hasArg();
}
