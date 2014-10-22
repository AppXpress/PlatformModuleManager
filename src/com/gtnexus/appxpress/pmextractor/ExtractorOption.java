package com.gtnexus.appxpress.pmextractor;

import com.gtnexus.appxpress.CLIOption;

/**
 * Enumeration of options that can be stored in the Properties file or read in from the CLI.
 */
public enum ExtractorOption implements CLIOption{
	
	HELP("help", String.class, "Display usage for this tool", false, false, null),
    PLATFORM_ZIP("platformZip", String.class, "Exported Platform Module Name", 
    		true, true, null),
    LOCAL_DIR("localDir",String.class, "Relative Path of GIT staging folder", 
    		true, true, null),
    CUSTOMER("customer", String.class, "Customer of Platform Module", 
    		true, true, null),
    PLATFORM("platform", String.class, "Platform Module that is being exported", 
    		true, true, null),
    OVERWRITE_SCRIPTS("overwriteScripts", Boolean.class, "If Y -> overwriteScripts = true", 
    		true, false, "N"),
    OVERWRITE_FEF("overwriteFef", Boolean.class, "If Y -> overwriteFEF = true", 
    		true, false, "N");

    private final String name;
    private final Class<?> type;
    private final boolean hasArg;
    private final boolean isMandatory;
    private final String defaultValue;
    private final String description;

    /**
     * 
     * @param name The name of this ExtractorOption.
     * @param type The ExtractorOption type.
     * @param description
     * @param isMandatory
     * @param defaulValue
     */
	private ExtractorOption(String name, Class<?> type, String description,
			boolean hasArg, boolean isMandatory, String defaulValue) {
		this.name = name;
		this.type = type;
		this.hasArg = hasArg;
		this.description = description;
		this.isMandatory = isMandatory;
		this.defaultValue = defaulValue;
	}

    @Override
    public String toString() {
        return name;
    }
    
    public String getName() {
    	return name;
    }

    public Class<?> getType() {
        return type;
    }

    public String getMessage() {
        if (type.equals(Integer.class)) {
            return ("Please enter the number of " + name
                    + "(s): ");
        } else if (type.equals(String.class)) {
            return ("Please enter " + name + ": ");
        } else if (type.equals(Boolean.class)) {
            return ("Do you want " + name + "? [y/n]: ");
        } else {
            return ""; //TODO this should throw an exception?
        }
    }
    
    public String getDescription() {
    	return description;
    }
    
    public boolean hasArg() {
    	return hasArg;
    }

    public boolean isMandatory() {
        return isMandatory;
    }

    /**
     * @return the default value for non-mandatory options.
     * @throws java.lang.UnsupportedOperationException if this method is called
     *                                                 on a mandatory option.
     */
    public String getDefaultValue() {
        if (this.isMandatory) {
            throw new UnsupportedOperationException(this.name + " " +
                    "is a mandatory field, and must come from " +
                    "user args or properties. There is no default value.");
        }
        return defaultValue;
    }

    //TODO this can be improved to make sure string input has actual meaning

    /**
     * Checks to see if the val supplies is a valid argument according
     * to this option's type.
     *
     * @param val
     * @return
     */
    public boolean isValid(String val) {
        if (val == null || val.length() == 0) {
            return false;
        }
        if (type.equals(String.class)) {
            return true;
        }
        if (type.equals(Integer.class)) {
            return val.matches("\\d+"); //TODO test this regex
        } else if (type.equals(Boolean.class)) {
            return val.equalsIgnoreCase("Y") || val.equalsIgnoreCase("N");
        } else {
            return false;
        }
    }
}
