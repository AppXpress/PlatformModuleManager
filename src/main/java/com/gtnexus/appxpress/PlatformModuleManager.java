package com.gtnexus.appxpress;

import java.util.Set;

import com.google.common.base.Optional;
import com.gtnexus.appxpress.cli.command.CLICommand;
import com.gtnexus.appxpress.cli.command.CommandParser;
import com.gtnexus.appxpress.commons.command.Command;
import com.gtnexus.appxpress.exception.AppXpressException;

public class PlatformModuleManager {
	
	public static void main(String ... args) {
		new PlatformModuleManager().run(args);
	}
	
	public PlatformModuleManager() {
	}
	
	protected void run(String ...args) {
		try {
			Command c = PlatformModuleManager.getCommand(this, args);
			c.execute();
		} catch (AppXpressException e) {
			System.out.println(e.getAppXpressMessage());
		}
	}
	
	public static Command getCommand(PlatformModuleManager pmm, String ...args) throws AppXpressException {
		Optional<Command> command =  new CommandParser(pmm.getCommands()).parse(args);
		return command.or(PlatformModuleManagerCommand.HELP.constructCommand(args));
	}

	public Set<CLICommand> getCommands() {
		return PlatformModuleManagerCommand.getAllCommands();
	}
}
