package com.gtnexus.pmm;

import java.util.Set;

import com.gtnexus.pmm.cli.command.CLICommand;
import com.gtnexus.pmm.cli.command.CommandParser;
import com.gtnexus.pmm.commons.DirectoryHelper;
import com.gtnexus.pmm.commons.command.Command;
import com.gtnexus.pmm.commons.properties.PMProperties;

public class PlatformModuleManager {

    public static void main(String... args) {
	try {
	 PlatformModuleManager pmm = start(args);
	 pmm.run();
	} catch(AppXpressException e) {
	    System.out.println("Failed to start.");
	}
    }
    
    public static PlatformModuleManager start(String ...args) throws AppXpressException{
	DirectoryHelper dHelper = new DirectoryHelper();
	dHelper.ensureAppXpress();
	return new PlatformModuleManager(dHelper.getPmProperties(), args);
    }
    
    private final PMProperties properties;
    private final String[] rawArgs;

    public PlatformModuleManager(PMProperties properties, String ...args) {
	this.properties = properties;
	this.rawArgs = args;
    }
    
    public PMProperties getProperties() {
        return properties;
    }

    public String[] getRawArgs() {
        return rawArgs;
    }
    
    protected void run() {
	try {
	    PlatformModuleManager.getCommand(this).execute();
	} catch (AppXpressException e) {
	    System.out.println(e.getAppXpressMessage());
	}
    }

    public static Command getCommand(PlatformModuleManager pmm) {
	return new CommandParser(pmm.getCommands())
		.parse(pmm.getRawArgs())
		.or(PlatformModuleManagerCommand.HELP.constructCommand(pmm.getRawArgs()));
    }

    public Set<CLICommand> getCommands() {
	return PlatformModuleManagerCommand.getAllCommands();
    }
}
