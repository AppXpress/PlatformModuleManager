package com.gtnexus.appxpress;

import java.util.Set;

import com.gtnexus.appxpress.cli.command.CLICommand;
import com.gtnexus.appxpress.cli.command.CommandParser;
import com.gtnexus.appxpress.commons.command.Command;
import com.gtnexus.appxpress.exception.AppXpressException;

public class PlatformModuleManager {

    public static void main(String... args) {
	new PlatformModuleManager().run(args);
    }

    public PlatformModuleManager() {
    }

    protected void run(String... args) {
	try {
	    PlatformModuleManager.getCommand(this, args).execute();
	} catch (AppXpressException e) {
	    System.out.println(e.getAppXpressMessage());
	}
    }

    public static Command getCommand(PlatformModuleManager pmm, String... args) {
	return new CommandParser(pmm.getCommands()).parse(args)
		.or(PlatformModuleManagerCommand.HELP.constructCommand(args));
    }

    public Set<CLICommand> getCommands() {
	return PlatformModuleManagerCommand.getAllCommands();
    }
}
