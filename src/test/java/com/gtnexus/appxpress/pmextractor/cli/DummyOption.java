package com.gtnexus.appxpress.pmextractor.cli;

import com.gtnexus.pmm.api.v100.cli.option.CommandOption;

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

}
