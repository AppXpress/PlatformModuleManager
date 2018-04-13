package com.gtnexus.pmm.manager;

import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;
import com.gtnexus.pmm.api.v100.command.AbstractSubCommand;
import com.gtnexus.pmm.api.v100.command.SubCommandMarker;
import com.gtnexus.pmm.api.v100.service.PlatformModuleManagerServices;
import com.gtnexus.pmm.manager.command.CLICommand;
import com.gtnexus.pmm.pmbuilder.BuildCommand;
import com.gtnexus.pmm.pmdocgen.DocGenCommand;
import com.gtnexus.pmm.pmextractor.ExtractCommand;

@SuppressWarnings("unchecked")
public class SubCommandScanner {

    private static boolean scanForPlugins = false;
    private static Set<Class<? extends AbstractSubCommand>> coreSubCommands = new ImmutableSet.Builder<Class<? extends AbstractSubCommand>>()
	    .add(BuildCommand.class)
	    .add(ExtractCommand.class)
	    .add(DocGenCommand.class)
	    .build();

    public static Set<CLICommand> scan() {
	Set<CLICommand> commands = new HashSet<>();
	commands.addAll(scanForCoreCommands());
	if(scanForPlugins) {
	    commands.addAll(scanForPluginCommands());
	}
	return commands;
    }

    private static Set<CLICommand> scanForCoreCommands() {
	Set<CLICommand> commands = new HashSet<>();
	for(Class<? extends AbstractSubCommand> type : coreSubCommands) {
	    SubCommandMarker marker = type.getAnnotation(SubCommandMarker.class);
	    CLICommand wrap = CLICommand.Helper.wrap(marker, getConstructor(type, marker));
	    commands.add(wrap);
	}
	return commands;
    }


    private static Set<CLICommand> scanForPluginCommands() {
	Set<CLICommand> commands = new HashSet<>();
	//TODO: This is a very wide initial scan. For external tools, it should be a known relative directory.
	ConfigurationBuilder b = new ConfigurationBuilder().forPackages("com.gtnexus.pmm");
	Reflections reflections = new Reflections(b);
	Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(SubCommandMarker.class);
	Set<Class<? extends AbstractSubCommand>> subTypesOf = reflections.getSubTypesOf(AbstractSubCommand.class);
	SetView<Class<?>> intersection = Sets.intersection(typesAnnotatedWith, subTypesOf);
	for (Class<?> type : intersection) {
	    SubCommandMarker marker = type.getAnnotation(SubCommandMarker.class);
	    CLICommand wrap = CLICommand.Helper.wrap(marker, getConstructor(type, marker));
	    commands.add(wrap);
	}
	return commands;
    }

    private static Constructor<? extends AbstractSubCommand> getConstructor(Class<?> type, SubCommandMarker marker) {
	Constructor<? extends AbstractSubCommand> constructor;
	try {
	    constructor = (Constructor<? extends AbstractSubCommand>) type
		    .getConstructor(PlatformModuleManagerServices.class, String[].class);
	} catch (NoSuchMethodException e) {
	    throw new RuntimeException(marker.name() + " is missing a constructor.", e);
	} catch (SecurityException e) {
	    throw new RuntimeException(
		    "Failure during startup. Access to " + marker.name() + "'s constructor was denied", e);
	}
	return constructor;
    }
}
