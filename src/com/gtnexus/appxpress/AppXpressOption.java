package com.gtnexus.appxpress;

/**
 * Extension of typical command line options interface. 
 * Holds AppXpress specific logic.
 * 
 * @author jdonovan
 *
 */
public interface AppXpressOption extends CLIOption {

	public String getMessage();

	public boolean isAppXpressMandatory();

	public boolean shouldBeOmitted();

	public boolean isValid(String val);

	public String getDefaultValue();

}
