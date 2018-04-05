package com.gtnexus.pmm.cli.option;

/**
 * Extension of typical command line options interface. Holds AppXpress specific
 * logic.
 * 
 * @author jdonovan
 *
 */
public interface CLICommandOption {
    @Override
    public String toString();

    public String getLongName();

    public String getFlag();

    public Class<?> getType();

    public String getDescription();

    public boolean hasArg();

    public boolean isHelpFlag();

    //TODO: remove
    public String getMessage();

    //TODO: remove
    public boolean isAppXpressMandatory();

    //TODO: remove
    public boolean shouldBeOmitted();

    //TODO:remove
    public boolean isValid(String val);

    public String getDefaultValue();

    //TODO: remove
    public boolean isStoreableProperty();
    
}
