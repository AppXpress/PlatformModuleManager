package com.gtnexus.pmm.pmdocgen.cli.option;

import com.gtnexus.pmm.cli.option.CLICommandOption;

public class DocumentGeneratorOptions implements CLICommandOption {

    @Override
    public String getLongName() {
	return null;
    }

    @Override
    public String getFlag() {
	return null;
    }

    @Override
    public Class<?> getType() {
	return null;
    }

    @Override
    public String getDescription() {
	return null;
    }

    @Override
    public boolean hasArg() {
	return false;
    }

    @Override
    public boolean isHelpFlag() {
	return false;
    }

    @Override
    public String getMessage() {
	return null;
    }

    @Override
    public boolean isAppXpressMandatory() {
	return false;
    }

    @Override
    public boolean shouldBeOmitted() {
	return false;
    }

    @Override
    public boolean isValid(String val) {
	return false;
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
