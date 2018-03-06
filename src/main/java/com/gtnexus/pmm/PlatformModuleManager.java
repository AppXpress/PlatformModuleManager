package com.gtnexus.pmm;

import java.util.Arrays;
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
	return new PlatformModuleManager(new PlatformModuleManagerServicesImpl(dHelper.getPmProperties(), args));
    }

    private final PlatformModuleManagerServices services;

    public PlatformModuleManager(PlatformModuleManagerServices services) {
	this.services = services;
    }

    public PMProperties getProperties() {
	return this.services.getEnvironmentService().getProperties();
    }

    public String[] getRawArgs() {
	return this.services.getEnvironmentService().getRawArgs();
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
	Command cmd;
	String[] args = pmm.getRawArgs();
	if (args.length > 0) {
	    String cmdNameOrFlag = args[0];
	    CLICommand cliCmd = new CommandIdentifier(pmm.getCommands())
		    .identify(cmdNameOrFlag)
		    .or(PlatformModuleManagerCommand.HELP);
	    cmd = cliCmd.constructCommand(pmm.getServices(), restOf(args));
	} else {
	    cmd = PlatformModuleManagerCommand.HELP.constructCommand(pmm.getServices(), args);
	}
	return cmd;
    }

    private static String[] restOf(String... args) {
	if (args.length < 2) {
	    return new String[0];
	}
	return Arrays.copyOfRange(args, 1, args.length);
    }

    public Set<CLICommand> getCommands() {
	return PlatformModuleManagerCommand.getAllCommands();
    }
}
