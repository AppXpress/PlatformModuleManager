package com.gtnexus.pmm.cli.command;

import com.gtnexus.pmm.PlatformModuleManagerServices;
import com.gtnexus.pmm.commons.command.Command;

public interface CLICommand {
    public String getName();

    public boolean isHelpFlag();

    public String getDescription();

    public String getFlag();

    public String getMessage();

    public Command constructCommand(PlatformModuleManagerServices services, String... args);
}
