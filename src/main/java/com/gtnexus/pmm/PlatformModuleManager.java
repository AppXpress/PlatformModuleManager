package com.gtnexus.pmm;

import java.util.Set;

import com.gtnexus.pmm.cli.command.CLICommand;
import com.gtnexus.pmm.cli.command.CommandIdentifier;
import com.gtnexus.pmm.commons.DirectoryHelper;
import com.gtnexus.pmm.commons.command.Command;
import com.gtnexus.pmm.commons.properties.PMProperties;

public class PlatformModuleManager {

    public static void main(String... args) {
	try {
	    PlatformModuleManager pmm = start(args);
	    pmm.run();
	} catch (AppXpressException e) {
	    System.out.println("Failed to start.");
	}
    }

    public static PlatformModuleManager start(String... args) throws AppXpressException {
	DirectoryHelper dHelper = new DirectoryHelper();
	dHelper.ensureAppXpress();
	// need to discover subcommands here.
	// need to start services here.
	return new PlatformModuleManager(
		new PlatformModuleManagerServicesImpl(), 
		dHelper.getPmProperties(), 
		args);
    }

    private final PMProperties properties;
    private final String[] rawArgs;
    private final PlatformModuleManagerServices services;

    public PlatformModuleManager(PlatformModuleManagerServices services, PMProperties properties, String... args) {
	this.properties = properties;
	this.rawArgs = args;
	this.services = services;
    }

    public PMProperties getProperties() {
	return properties;
    }

    public String[] getRawArgs() {
	return rawArgs;
    }
    
    public PlatformModuleManagerServices getServices() {
	return this.services;
    }

    protected void run() {
	try {
	    PlatformModuleManager.getCommand(this).execute();
	} catch (AppXpressException e) {
	    System.out.println(e.getAppXpressMessage());
	}
    }

    public static Command getCommand(PlatformModuleManager pmm) {
	return new CommandIdentifier(pmm.getCommands())
		.identify(pmm.getRawArgs())
		.or(PlatformModuleManagerCommand.HELP.constructCommand(pmm.getRawArgs()));
    }

    public Set<CLICommand> getCommands() {
	return PlatformModuleManagerCommand.getAllCommands();
    }
}
