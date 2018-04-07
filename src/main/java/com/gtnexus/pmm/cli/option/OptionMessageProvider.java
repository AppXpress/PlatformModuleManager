package com.gtnexus.pmm.cli.option;

import com.gtnexus.pmm.api.v100.command.Command;

public class OptionMessageProvider {

    public String getMessage(Class<?> type, String name) {
	if (type.equals(Command.class)) {
	    return "Please enter a valid command.";
	} else if (type.equals(Integer.class)) {
	    return ("Please enter the number of " + name + "(s): ");
	} else if (type.equals(String.class)) {
	    return ("Please enter " + name + ": ");
	} else if (type.equals(Boolean.class)) {
	    return ("Do you want " + name + "? [y/n]: ");
	}
	throw new RuntimeException("Option type" + type.toString() + " is unsupported");
    }

}
