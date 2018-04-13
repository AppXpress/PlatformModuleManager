package com.gtnexus.pmm;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.gtnexus.pmm.api.v100.command.SubCommand;
import com.gtnexus.pmm.api.v100.service.PlatformModuleManagerServices;
import com.gtnexus.pmm.common.properties.PMProperties;
import com.gtnexus.pmm.manager.DirectoryHelper;
import com.gtnexus.pmm.manager.Help;
import com.gtnexus.pmm.manager.SubCommandScanner;
import com.gtnexus.pmm.manager.command.CLICommand;
import com.gtnexus.pmm.manager.command.CommandIdentifier;
import com.gtnexus.pmm.service.PlatformModuleManagerServicesImpl;

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
	Set<CLICommand> commands = SubCommandScanner.scan();
	Help help = new Help(commands);
	PlatformModuleManagerServicesImpl services = new PlatformModuleManagerServicesImpl(dHelper.getPmProperties(), args);
	return new PlatformModuleManager(services, commands, help);
    }

    private final PlatformModuleManagerServices services;
    private final Set<CLICommand> commands;
    private final Help help;

    public PlatformModuleManager(PlatformModuleManagerServices services, Set<CLICommand> commands, Help help) {
	this.services = services;
	this.commands = commands;
	this.help = help;
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
    
    Help getHelp() {
	return this.help;
    }

    protected void run() {
	try {
	    PlatformModuleManager.getCommand(this).execute();
	} catch (AppXpressException e) {
	    System.out.println(e.getAppXpressMessage());
	}
    }

    public static SubCommand getCommand(PlatformModuleManager pmm) {
	SubCommand  cmd;
	String[] args = pmm.getRawArgs();
	if (args.length > 0) {
	    String cmdNameOrFlag = args[0];
	    Set<CLICommand> allCmds = new HashSet<>(pmm.getCommands());
	    allCmds.add(pmm.getHelp());
	    CLICommand cliCmd = new CommandIdentifier(allCmds)
		    .identify(cmdNameOrFlag)
		    .or(pmm.getHelp());
	    cmd = cliCmd.constructCommand(pmm.getServices(), restOf(args));
	} else {
	    cmd = pmm.getHelp().constructCommand(pmm.getServices(), args);
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
	//return PlatformModuleManagerCommand.getAllCommands();
	return this.commands;
    }
}
