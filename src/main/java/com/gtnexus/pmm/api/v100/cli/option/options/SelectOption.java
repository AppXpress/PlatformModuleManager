package com.gtnexus.pmm.api.v100.cli.option.options;

import com.gtnexus.pmm.api.v100.cli.option.AbstractCommandOption;
import com.gtnexus.pmm.api.v100.cli.option.CommandOption;

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

}
