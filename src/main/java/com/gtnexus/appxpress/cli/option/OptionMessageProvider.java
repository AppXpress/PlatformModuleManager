package com.gtnexus.appxpress.cli.option;

public class OptionMessageProvider {
	
	public String getMessage(Class<?> type, String name) {
		if (type.equals(Integer.class)) {
			return ("Please enter the number of " + name + "(s): ");
		} else if (type.equals(String.class)) {
			return ("Please enter " + name + ": ");
		} else if (type.equals(Boolean.class)) {
			return ("Do you want " + name + "? [y/n]: ");
		}
		throw new RuntimeException("ExtractorOption type" + type.toString()
				+ " is unsupported");
	}

}
