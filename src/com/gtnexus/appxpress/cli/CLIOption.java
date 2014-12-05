package com.gtnexus.appxpress.cli;


public interface CLIOption {
	public String toString();
    public String getLongName();
    public String getFlag();
    public Class<?> getType();
    public String getDescription();
    public boolean hasArg();
}
