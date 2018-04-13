package com.gtnexus.pmm.api.v100.cli.option.options;

import com.gtnexus.pmm.api.v100.cli.option.AbstractCommandOption;
import com.gtnexus.pmm.api.v100.cli.option.CommandOption;

public class ModuleOption  extends AbstractCommandOption implements CommandOption {

    @Override
    public String getLongName() {
	return "module";
    }

    @Override
    public String getFlag() {
	return "m";
    }

    @Override
    public Class<?> getType() {
	return String.class;
    }

    @Override
    public String getDescription() {
	return "The name of the module";
    }

    @Override
    public boolean hasArg() {
	return true;
    }

}
