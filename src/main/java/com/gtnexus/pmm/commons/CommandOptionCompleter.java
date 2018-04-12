package com.gtnexus.pmm.commons;

import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.gtnexus.pmm.api.v100.service.EnvironmentService;
import com.gtnexus.pmm.api.v100.service.PlatformModuleManagerServices;
import com.gtnexus.pmm.cli.option.CommandOption;
import com.gtnexus.pmm.cli.option.CommandOption.StandardOptions;
import com.gtnexus.pmm.cli.prompt.Prompt;
import com.gtnexus.pmm.cli.prompt.SimplePrompt;

public class CommandOptionCompleter {

    private final PlatformModuleManagerServices services;
    private final Set<CommandOption> requiredOptions;
    private final Map<CommandOption, String> defaults;
    
    public CommandOptionCompleter(PlatformModuleManagerServices services, Set<CommandOption> requiredOptions) {
	this(services, requiredOptions, Collections.<CommandOption, String>emptyMap());
    }
    
    
    public CommandOptionCompleter(PlatformModuleManagerServices services, Set<CommandOption> requiredOptions, Map<CommandOption, String> defaults) {
	this.services = services;
	this.requiredOptions = requiredOptions;
	this.defaults = defaults;
    }

    public Map<CommandOption, String> complete(final Map<CommandOption, String> options) {
	Map<CommandOption, String> consolidated = new HashMap<>(options);
	if (!consolidated.containsKey(StandardOptions.LOCAL_DIR)) {
	    consolidated.put(StandardOptions.LOCAL_DIR, services.getEnvironmentService().getLocalDir().toString());
	}
	inheritOptionsFromWorkingDir(consolidated);
	if (consolidated.containsKey(StandardOptions.SELECT)) {
	    doSelect(consolidated);
	}
	for(CommandOption requiredOption : requiredOptions) {
	    if(!consolidated.containsKey(requiredOption)) {
		consolidated.put(requiredOption, getParameterFromUser(requiredOption));
	    }
	}
	for(Entry<CommandOption, String> entry : defaults.entrySet()) {
	   if(!consolidated.containsKey(entry.getKey())) {
	       consolidated.put(entry.getKey(), entry.getValue());
	   }
	}
	return consolidated;
    }

    private void doSelect(Map<CommandOption, String> consolidated) {
	throw new RuntimeException("Unimplemented");
    }

    protected void inheritOptionsFromWorkingDir(Map<CommandOption, String> consolidated) {
	if(consolidated.containsKey(StandardOptions.CUSTOMER) && consolidated.containsKey(StandardOptions.MODULE)) {
	    return;
	}
	EnvironmentService environmentService = services.getEnvironmentService();
	Path cwd = environmentService.getCWD().toAbsolutePath();
	Path localDir = environmentService.getLocalDir().toAbsolutePath();
	if(!cwd.equals(localDir) && cwd.startsWith(localDir)) {
	    Path parent = cwd.getParent();
	    if(parent.endsWith("customer")) {
		consolidated.put(StandardOptions.CUSTOMER, filename(cwd));
	    } else if(parent.getParent().endsWith("customer")) {
		consolidated.put(StandardOptions.CUSTOMER, filename(parent));
		consolidated.put(StandardOptions.MODULE, filename(cwd));
	    }
	}
    }
    
    private String filename(Path p) {
	return p.getFileName().toString();
    }
    
    private String getParameterFromUser(CommandOption option) {
	Prompt<String> p = new SimplePrompt(System.in, System.out); 
	String val = p.prompt(getMessage(option));
	while (!isValid(val, option.getType())) {
	    val = p.prompt(Prompt.INVALID_INPUT);
	}
	return val;
    }
    
    private String getMessage(CommandOption option) {
	Class<?> type = option.getType();
	String name = option.getLongName();
	if (type.equals(Integer.class)) {
	    return ("Please enter the number of " + name + "(s): ");
	} else if (type.equals(String.class)) {
	    return ("Please enter " + name + ": ");
	} else if (type.equals(Boolean.class)) {
	    return ("Do you want " + name + "? [y/n]: ");
	}
	throw new IllegalArgumentException("Option type" + type.toString() + " is unsupported");
    }

    public boolean isValid(String val, Class<?> type) {
	if (val == null || val.length() == 0) {
	    return false;
	}
	if (type.equals(String.class)) {
	    return true;
	}
	if (type.equals(Integer.class)) {
	    return val.matches("\\d+");
	} else if (type.equals(Boolean.class)) {
	    return val.equalsIgnoreCase("Y") || val.equalsIgnoreCase("N");
	} else {
	    return false;
	}
    }
}
