package com.gtnexus.pmm.manager;

import java.util.Set;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.gtnexus.pmm.api.v100.command.AbstractSubCommand;
import com.gtnexus.pmm.api.v100.service.PlatformModuleManagerServices;
import com.gtnexus.pmm.manager.command.CLICommand;

public class Help implements CLICommand {

    private final Set<CLICommand> commands;

    public Help(Set<CLICommand> commands) {
	this.commands = commands;
    }

    @Override
    public String getName() {
	return "help";
    }

    @Override
    public String getDescription() {
	return "display this message";
    }

    @Override
    public String getFlag() {
	return "h";
    }

    @Override
    public AbstractSubCommand constructCommand(PlatformModuleManagerServices services, String... args) {
	return new AbstractSubCommand(services, args) {
	    
	    private final String name = "help";
	    private final String blurb = Joiner.on("\n")
		    .join(Iterables.transform(commands, new Function<CLICommand, String>() {
			@Override
			public String apply(CLICommand cmd) {
			    StringBuilder b = new StringBuilder();
			    b.append(cmd.getName());
			    if (cmd.getFlag() != null && !cmd.getFlag().isEmpty()) {
				b.append(" (-").append(cmd.getFlag()).append(")");
			    } else {
				b.append("\t");
			    }
			    b.append("\t").append(cmd.getDescription());
			    return b.toString();
			}
		    }));

	    private final String template = "Welcome to the AppXpress Platform Module Manager\n\n%s";
	    
	    @Override
	    public String getName() {
		return name;
	    }

	    @Override
	    public void execute() {
		System.out.println(String.format(template, blurb));
		System.exit(0);
	    }

	};
    }

}
