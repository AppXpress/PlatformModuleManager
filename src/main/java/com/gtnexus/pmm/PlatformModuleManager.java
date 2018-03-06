package com.gtnexus.pmm;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;
import com.gtnexus.pmm.cli.command.CLICommand;
import com.gtnexus.pmm.cli.command.CommandIdentifier;
import com.gtnexus.pmm.commons.DirectoryHelper;
import com.gtnexus.pmm.commons.command.AbstractSubCommand;
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
	Set<CLICommand> commands = scan();
	PlatformModuleManagerServicesImpl services = new PlatformModuleManagerServicesImpl(dHelper.getPmProperties(), args);
	return new PlatformModuleManager(services, commands);
    }

    public static Set<CLICommand> scan() {
	Set<CLICommand> coreCommands = new HashSet<>();
	ConfigurationBuilder b = new ConfigurationBuilder().forPackages("com.gtnexus.pmm");
	Reflections reflections = new Reflections(b);
	Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(SubCommandMarker.class);
	Set<Class<? extends AbstractSubCommand>> subTypesOf = reflections.getSubTypesOf(AbstractSubCommand.class);
	SetView<Class<?>> intersection = Sets.intersection(typesAnnotatedWith, subTypesOf);
	for (Class<?> type : intersection) {
	    SubCommandMarker marker = type.getAnnotation(SubCommandMarker.class);
	    Constructor<? extends AbstractSubCommand> constructor;
	    try {
		constructor = (Constructor<? extends AbstractSubCommand>) type.getConstructor(PlatformModuleManagerServices.class, String[].class);
	    } catch (NoSuchMethodException e) {
		throw new RuntimeException(marker.name() + " is missing a constructor.", e);
	    } catch (SecurityException e) {
		throw new RuntimeException("Failure during startup. Access to " + marker.name() + "'s constructor was denied", e);
	    }
	   coreCommands.add(CLICommand.Helper.wrap(marker, constructor)); 
	}
	return coreCommands;
    }

    private final PlatformModuleManagerServices services;
    private final Set<CLICommand> commands;

    public PlatformModuleManager(PlatformModuleManagerServices services, Set<CLICommand> commands) {
	this.services = services;
	this.commands = commands;
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
	//return PlatformModuleManagerCommand.getAllCommands();
	return this.commands;
    }
}
