package com.gtnexus.pmm;

import java.util.Set;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.gtnexus.pmm.cli.command.CLICommand;
import com.gtnexus.pmm.commons.command.Command;

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
    public Command constructCommand(PlatformModuleManagerServices services, String... args) {
	return new Command() {
	    private final String blurb = Joiner.on("\n")
		    .join(Iterables.transform(commands, new Function<CLICommand, String>() {
			@Override
			public String apply(CLICommand cmd) {
			    StringBuilder b = new StringBuilder();
			    b.append(cmd.getName());
			    if (cmd.getFlag() != null) {
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
	    public void execute() {
		System.out.println(String.format(template, blurb));
		System.exit(0);
	    }

	};
    }

}