package com.gtnexus.appxpress.pmextractor;

/**
 * Enumeration of options that can be stored in the Properties file or read in from the CLI.
 */
public enum ExtractorOption {
    PLATFORM_ZIP("platformZip", String.class), LOCAL_DIR("localDir",
            String.class), CUSTOMER("customer", String.class), PLATFORM(
            "platform", String.class), OVERWRITE_SCRIPTS(
            "overwriteScripts", Boolean.class), OVERWRITE_FEF(
            "overwriteFed", Boolean.class);

    private final String name;
    private final Class<?> type;

    /**
     * @param name The name of this ExtractorOption.
     * @param type The ExtractorOption type.
     */
    private ExtractorOption(String name, Class<?> type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public String toString() {
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

    public boolean isValid(String val) {
        if(val == null || val.length() == 0) {
            return false;
        }
        if(type.equals(String.class)) {
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
