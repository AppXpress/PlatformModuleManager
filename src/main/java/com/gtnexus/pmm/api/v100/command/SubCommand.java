package com.gtnexus.pmm.api.v100.command;

import java.util.Set;

import com.gtnexus.pmm.cli.option.CommandOption;

public interface SubCommand extends Command{
    public String getName();

    public String getHelpHeader();

    public String getHelpFooter();

    public  Set<CommandOption> getOptions();
}
