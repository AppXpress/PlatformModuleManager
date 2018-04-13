package com.gtnexus.pmm.api.v100.command;

import java.util.Set;

import com.gtnexus.pmm.AppXpressException;
import com.gtnexus.pmm.cli.option.CommandOption;

public interface SubCommand {
    public String getName();

    public String getHelpHeader();

    public String getHelpFooter();

    public  Set<CommandOption> getOptions();
    
    public void execute() throws AppXpressException;
}
