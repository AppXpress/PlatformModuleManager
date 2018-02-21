package com.gtnexus.appxpress.pmextractor.cli;

import com.gtnexus.appxpress.cli.option.CLIOption;

public enum DummyOption implements CLIOption {

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

}
