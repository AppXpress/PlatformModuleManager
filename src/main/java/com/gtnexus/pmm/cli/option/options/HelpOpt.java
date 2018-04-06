package com.gtnexus.pmm.cli.option.options;

import com.gtnexus.pmm.cli.option.CommandOption;

public class HelpOpt implements CommandOption {

    @Override
    public String getLongName() {
	return "help";
    }

    @Override
    public String getFlag() {
	return "h";
    }

    @Override
    public Class<?> getType() {
	return String.class;
    }

    @Override
    public String getDescription() {
	return "Display usage for this tool";
    }

    @Override
    public boolean hasArg() {
	return false;
    }

    @Override
    public boolean isHelpFlag() {
	return true;
    }

    @Override
    public String getMessage() {
	return "we have no msg :(";
    }

    @Override
    public boolean isAppXpressMandatory() {
	return false;
    }

    @Override
    public boolean shouldBeOmitted() {
	return true;
    }

    @Override
    public boolean isValid(String val) {
	return true;
    }

    @Override
    public String getDefaultValue() {
	return null;
    }

    @Override
    public boolean isStoreableProperty() {
	return false;
    }

}
