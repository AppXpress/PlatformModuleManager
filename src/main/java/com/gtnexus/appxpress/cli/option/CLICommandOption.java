package com.gtnexus.appxpress.cli.option;

/**
 * Extension of typical command line options interface. 
 * Holds AppXpress specific logic.
 * 
 * @author jdonovan
 *
 */
public interface CLICommandOption extends CLIOption {

	public String getMessage();
	public boolean isAppXpressMandatory();
	public boolean shouldBeOmitted();
	public boolean isValid(String val);
	public String getDefaultValue();
	public boolean isStoreableProperty();

}
