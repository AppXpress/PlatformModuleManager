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

    public String getMessage();

    public boolean isAppXpressMandatory();

    public boolean shouldBeOmitted();

    public boolean isValid(String val);

    public String getDefaultValue();

    public boolean isStoreableProperty();

}
