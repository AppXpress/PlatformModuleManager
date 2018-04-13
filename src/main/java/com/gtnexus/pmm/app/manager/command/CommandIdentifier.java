package com.gtnexus.pmm.app.manager.command;

import java.util.Set;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

public class CommandIdentifier {

    private final Set<CLICommand> commands;

    public CommandIdentifier(Set<CLICommand> commands) {
	Preconditions.checkNotNull(commands);
	Preconditions.checkArgument(commands.size() > 0);
	this.commands = commands;
    }

    public Optional<CLICommand> identify(String cmdNameOrFlag) {
	if (cmdNameOrFlag == null || cmdNameOrFlag.isEmpty()) {
	    return Optional.absent();
	}
	for (CLICommand command : commands) {
	    if (isNameOrFlagMatch(command, cmdNameOrFlag)) {
		return Optional.fromNullable(command);
	    }
	}
	return Optional.absent();
    }

    private boolean isNameOrFlagMatch(CLICommand command, String cmd) {
	if (command.getName().equals(cmd)) {
	    return true;
	}
	if (command.getFlag() != null && cmd.startsWith("-")) {
	    if (command.getFlag().equals(cmd.substring(1, cmd.length()))) {
		return true;
	    }
	}
	return false;
    }



}
