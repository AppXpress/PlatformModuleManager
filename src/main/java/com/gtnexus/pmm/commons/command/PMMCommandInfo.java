package com.gtnexus.pmm.commons.command;

import java.util.Set;

import com.gtnexus.pmm.cli.option.CommandOption;

public interface PMMCommandInfo {

    public String getName();

    public String getHelpHeader();

    public String getHelpFooter();

    public  Set<CommandOption> getOptions();
    
}
