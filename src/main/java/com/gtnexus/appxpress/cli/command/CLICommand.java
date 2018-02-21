package com.gtnexus.appxpress.cli.command;

import com.gtnexus.appxpress.commons.command.Command;

public interface CLICommand {
    public String getName();

    public boolean isHelpFlag();

    public String getDescription();

    public String getFlag();

    public String getMessage();

    public Command constructCommand(String... args);
}
