package com.gtnexus.pmm.commons.command;

import java.util.Set;

import com.gtnexus.pmm.cli.option.CLICommandOption;

public interface PMMCommandInfo {

    public String getName();

    public String getHelpHeader();

    public String getHelpFooter();

    public  Set<CLICommandOption> getOptions();
    
}
