package com.gtnexus.appxpress.pmextractor.cli;

import com.gtnexus.appxpress.cli.CLIOption;


public class DummyOption implements CLIOption{

	@Override
	public String getName() {
		return "Dummy";
	}

	@Override
	public Class<?> getType() {
		// TODO Auto-generated method stub
		return Object.class;
	}

	@Override
	public String getDescription() {
		return "Dummy";
	}

	@Override
	public boolean hasArg() {
		return false;
	}

}
