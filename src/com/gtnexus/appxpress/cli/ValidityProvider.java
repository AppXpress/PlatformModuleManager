package com.gtnexus.appxpress.cli;

public class ValidityProvider {

	public boolean isValid(String val, Class<?> type) {
		// TODO this can be improved to make sure string input has actual
		// meaning
		if (val == null || val.length() == 0) {
			return false;
		}
		if (type.equals(String.class)) {
			return true;
		}
		if (type.equals(Integer.class)) {
			return val.matches("\\d+"); // TODO test this regex
		} else if (type.equals(Boolean.class)) {
			return val.equalsIgnoreCase("Y") || val.equalsIgnoreCase("N");
		} else {
			return false;
		}
	}

}
