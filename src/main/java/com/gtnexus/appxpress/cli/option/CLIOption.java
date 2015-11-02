package com.gtnexus.appxpress.cli.option;


public interface CLIOption {
	@Override
	public String toString();
    public String getLongName();
    public String getFlag();
    public Class<?> getType();
    public String getDescription();
    public boolean hasArg();
    public boolean isHelpFlag();
}
