package com.gtnexus.pmm.app.manager;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.Collections;
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
import com.gtnexus.pmm.app.build.BuildCommand;
import com.gtnexus.pmm.app.docgen.DocGenCommand;
import com.gtnexus.pmm.app.extract.ExtractCommand;
import com.gtnexus.pmm.app.manager.command.CLICommand;

@SuppressWarnings("unchecked")
public class SubCommandScanner {

    private static final String PLUGIN_PKG = "com.gtnexus.pmm.plugin";
    private static boolean scanForPlugins = true;
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
	URL[] pluginUrls = getPluginUrls();
	if(pluginUrls.length == 0) {
	    return Collections.emptySet();
	}
	return scanUrls(pluginUrls);
    }

    private static Set<CLICommand> scanUrls(URL[] pluginUrls) {
	Set<CLICommand> commands = new HashSet<>();
	ConfigurationBuilder b = createConfiguration(pluginUrls);
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

    private static ConfigurationBuilder createConfiguration(URL[] pluginUrls) {
	URLClassLoader classLoader = new URLClassLoader(pluginUrls);
	ConfigurationBuilder b = new ConfigurationBuilder()
		.addClassLoader(classLoader)
		.addUrls(pluginUrls)
		.forPackages(PLUGIN_PKG);
	return b;
    }
    
    static PathMatcher pm = FileSystems.getDefault().getPathMatcher("glob:**.jar");

    static DirectoryStream.Filter<Path> pf = new DirectoryStream.Filter<Path>() {

	@Override
	public boolean accept(Path entry) throws IOException {
	    boolean matches = pm.matches(entry);
	    return matches;
	}

    };


    private static URL[] getPluginUrls() {
	Set<URL> urlSet = new HashSet<>();
	String preamble = "Exception during start up. ";
	String notExistsTemplate = preamble + "PlugIn path %s does not exist";
	String notADirTemplate =   preamble + "Plugin path %s is not a directory"; 
	Path pluginPath = Paths.get("/code/gtnexus/appx/pmm-upload/build/libs/");
	if(!Files.exists(pluginPath)) {
	    throw new RuntimeException(String.format(notExistsTemplate, pluginPath.toAbsolutePath().toString()));
	} else if(!Files.isDirectory(pluginPath)) {
	    throw new RuntimeException(String.format(notADirTemplate, pluginPath.toAbsolutePath().toString()));
	}
	try(DirectoryStream<Path> pluginDirectoryStream = Files.newDirectoryStream(pluginPath, pf)) {
	    for(Path p : pluginDirectoryStream) {
		urlSet.add(p.toUri().toURL());
	    }
	} catch(IOException e) {
	    throw new RuntimeException(preamble, e);
	}
	return urlSet.toArray(new URL[urlSet.size()]);
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
