package com.gtnexus.pmm.app;

import java.util.HashSet;
import java.util.Set;

import com.gtnexus.pmm.api.v100.PMProperties;
import com.gtnexus.pmm.api.v100.command.SubCommand;
import com.gtnexus.pmm.api.v100.command.SubCommandException;
import com.gtnexus.pmm.api.v100.service.PlatformModuleManagerServices;
import com.gtnexus.pmm.app.manager.DirectoryHelper;
import com.gtnexus.pmm.app.manager.Help;
import com.gtnexus.pmm.app.manager.SubCommandScanner;
import com.gtnexus.pmm.app.manager.command.CLICommand;
import com.gtnexus.pmm.app.manager.command.CommandIdentifier;
import com.gtnexus.pmm.app.service.PlatformModuleManagerServicesImpl;

public class PlatformModuleManager {

    public static void main(String... args) {
	try {
	    PlatformModuleManager pmm = start(args);
	    pmm.run();
	} catch (SubCommandException e) {
	    System.out.println("Failed to start.");
	}
    }

    public static PlatformModuleManager start(String... args) throws SubCommandException {
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
	} catch (SubCommandException e) {
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
	    cmd = cliCmd.constructCommand(pmm.getServices(), args);
	} else {
	    cmd = pmm.getHelp().constructCommand(pmm.getServices(), args);
	}
	return cmd;
    }

    public Set<CLICommand> getCommands() {
	//return PlatformModuleManagerCommand.getAllCommands();
	return this.commands;
    }
}
