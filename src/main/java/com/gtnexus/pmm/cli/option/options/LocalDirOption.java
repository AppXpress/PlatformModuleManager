package com.gtnexus.pmm.cli.option.options;

import com.gtnexus.pmm.cli.option.AbstractCommandOption;
import com.gtnexus.pmm.cli.option.CommandOption;

public class LocalDirOption  extends AbstractCommandOption implements CommandOption {
    
    public static final String  NAME = "localDir";
    public static final String  FLAG = "ld";

    @Override
    public String getLongName() {
	return NAME;
    }

    @Override
    public String getFlag() {
	return FLAG;
    }

    @Override
    public Class<?> getType() {
	return String.class;
    }

    @Override
    public String getDescription() {
	return "Relative path of git staging folder.";
    }

    @Override
    public boolean hasArg() {
	return true;
    }

}
