package com.gtnexus.pmm.cli.option.options;

import com.gtnexus.pmm.cli.option.AbstractCommandOption;
import com.gtnexus.pmm.cli.option.CommandOption;

public class SelectOption  extends AbstractCommandOption implements CommandOption {

    @Override
    public String getLongName() {
	return "select";
    }

    @Override
    public String getFlag() {
	return "s";
    }

    @Override
    public Class<?> getType() {
	return Integer.class;
    }

    @Override
    public String getDescription() {
	return "Select module from the folders present in the cwd.";
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
    public boolean isAppXpressMandatory() {
	return false;
    }

    @Override
    public boolean shouldBeOmitted() {
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
