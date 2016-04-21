package com.gtnexus.appxpress.cli.command;

import java.util.Arrays;
import java.util.Set;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.gtnexus.appxpress.commons.command.Command;

public class CommandParser {
	
	private final Set<CLICommand> commands;
	
	public CommandParser(Set<CLICommand> commands) {
		Preconditions.checkNotNull(commands);
		Preconditions.checkArgument(commands.size() > 0);
		this.commands = commands;
	}

	//we assume the first argument is either a valid command or a help flag
	public Optional<Command> parse(String... args) {
		if (args.length == 0) {
			return Optional.absent();
		}
		String cmd = args[0];
		String[] childArgs = restOf(args);
		for(CLICommand command : commands) {
			if(isNameOrFlagMatch(command, cmd)) {
				return Optional.fromNullable(command.constructCommand(childArgs));
			} 
		}
		System.out.println("Not a recognized command" + cmd);
		return Optional.absent();
	}
	
	private boolean isNameOrFlagMatch(CLICommand command, String cmd) {
		if(command.getName().equals(cmd)) {
			return true;
		}
		if(command.getFlag() != null && cmd.startsWith("-")) {
			if(command.getFlag().equals(cmd.substring(1, cmd.length()))) {
				return true;
			}
		}
		return false;
	}
	
	private String[] restOf(String ...args) {
	if(args.length < 2) {
		return new String[0];
	}
	return Arrays.copyOfRange(args, 1, args.length);
}

}
