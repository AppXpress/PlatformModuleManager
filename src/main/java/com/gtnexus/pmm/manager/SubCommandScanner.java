package com.gtnexus.pmm.manager;

import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;
import com.gtnexus.pmm.api.v100.command.AbstractSubCommand;
import com.gtnexus.pmm.api.v100.command.SubCommandMarker;
import com.gtnexus.pmm.api.v100.service.PlatformModuleManagerServices;
import com.gtnexus.pmm.cli.command.CLICommand;

public class SubCommandScanner {

    @SuppressWarnings("unchecked")
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
		constructor = (Constructor<? extends AbstractSubCommand>) type
			.getConstructor(PlatformModuleManagerServices.class, String[].class);
	    } catch (NoSuchMethodException e) {
		throw new RuntimeException(marker.name() + " is missing a constructor.", e);
	    } catch (SecurityException e) {
		throw new RuntimeException(
			"Failure during startup. Access to " + marker.name() + "'s constructor was denied", e);
	    }
	    CLICommand wrap = CLICommand.Helper.wrap(marker, constructor);
	    coreCommands.add(wrap);
	}
	return coreCommands;
    }
}
