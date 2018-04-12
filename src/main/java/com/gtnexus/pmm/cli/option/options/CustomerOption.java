package com.gtnexus.pmm.cli.option.options;

import com.gtnexus.pmm.cli.option.AbstractCommandOption;
import com.gtnexus.pmm.cli.option.CommandOption;

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
