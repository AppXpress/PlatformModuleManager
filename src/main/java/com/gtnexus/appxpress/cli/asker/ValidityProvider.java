package com.gtnexus.appxpress.cli.asker;

public class ValidityProvider {

    public boolean isValid(String val, Class<?> type) {
	if (val == null || val.length() == 0) {
	    return false;
	}
	if (type.equals(String.class)) {
	    return true;
	}
	if (type.equals(Integer.class)) {
	    return val.matches("\\d+");
	} else if (type.equals(Boolean.class)) {
	    return val.equalsIgnoreCase("Y") || val.equalsIgnoreCase("N");
	} else {
	    return false;
	}
    }

}
