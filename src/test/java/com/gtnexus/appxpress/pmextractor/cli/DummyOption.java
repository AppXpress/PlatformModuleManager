package com.gtnexus.appxpress.pmextractor.cli;

import com.gtnexus.pmm.cli.option.CommandOption;

public enum DummyOption implements CommandOption {

    DUMMY;

    @Override
    public String getLongName() {
	return "Dummy";
    }

    @Override
    public Class<?> getType() {
	return String.class;
    }

    @Override
    public String getDescription() {
	return "Dummy description";
    }

    @Override
    public boolean hasArg() {
	return false;
    }

    @Override
    public String getFlag() {
	return "d";
    }

    @Override
    public boolean isHelpFlag() {
	return false;
    }

    @Override
    public boolean isAppXpressMandatory() {
	// TODO Auto-generated method stub
	return false;
    }

    @Override
    public boolean shouldBeOmitted() {
	// TODO Auto-generated method stub
	return false;
    }

    @Override
    public boolean isValid(String val) {
	// TODO Auto-generated method stub
	return false;
    }

    @Override
    public String getDefaultValue() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public boolean isStoreableProperty() {
	// TODO Auto-generated method stub
	return false;
    }

}
