package com.gtnexus.appxpress;


public interface CLIOption {
	public String toString();
    public String getName();
    public Class<?> getType();
    public String getDescription();
    public boolean hasArg();
    
    public String getMessage();
    public boolean isAppXpressMandatory();
    public boolean shouldBeOmitted();
    public boolean isValid(String val);
    public String getDefaultValue();
}
