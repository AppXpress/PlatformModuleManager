package com.gtnexus.appxpress.pmdocgen.cli.option;

import com.gtnexus.appxpress.cli.option.CLICommandOption;

public class HelpOpt implements CLICommandOption {

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
