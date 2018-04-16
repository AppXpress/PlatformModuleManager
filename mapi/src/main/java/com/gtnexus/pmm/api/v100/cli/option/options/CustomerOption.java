package com.gtnexus.pmm.api.v100.cli.option.options;

import com.gtnexus.pmm.api.v100.cli.option.AbstractCommandOption;
import com.gtnexus.pmm.api.v100.cli.option.CommandOption;

public class CustomerOption extends AbstractCommandOption implements CommandOption {

    @Override
    public String getLongName() {
	return "customer";
    }

    @Override
    public String getFlag() {
	return "c";
    }

    @Override
    public Class<?> getType() {
	return String.class;
    }

    @Override
    public String getDescription() {
	return "The customer who owns this module";
    }

    @Override
    public boolean hasArg() {
	return true;
    }

}
