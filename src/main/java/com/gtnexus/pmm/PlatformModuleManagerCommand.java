package com.gtnexus.pmm;

import java.util.Set;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.gtnexus.pmm.cli.command.CLICommand;
import com.gtnexus.pmm.cli.option.OptionMessageProvider;
import com.gtnexus.pmm.commons.command.Command;
import com.gtnexus.pmm.pmbuilder.BuildCommand;
import com.gtnexus.pmm.pmdocgen.PMDocGenCommand;
import com.gtnexus.pmm.pmextractor.ExtractCommand;

public abstract class PlatformModuleManagerCommand implements CLICommand {

    /**
     * 
     */
    public static final PlatformModuleManagerCommand HELP = new PlatformModuleManagerCommand() {

	@Override
	public String getName() {
	    return "help";
	}

	@Override
	public String getFlag() {
	    return "h";
	}

	@Override
	public String getDescription() {
	    return "displays this message";
	}

	@Override
	public boolean isHelpFlag() {
	    return true;
	}

	@Override
	public Command constructCommand(PlatformModuleManagerServices services, String... args) {
	    return new Command() {
		private final String blurb = Joiner.on("\n")
			.join(Iterables.transform(ALL_COMMANDS, new Function<CLICommand, String>() {
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
    };

    /**
     * 
     */
    public static final PlatformModuleManagerCommand BUILD = new PlatformModuleManagerCommand() {

	@Override
	public String getName() {
	    return "build";
	}

	@Override
	public String getDescription() {
	    return "runs the platform module builder tool. for more information please run pmm build -h";
	}

	@Override
	public Command constructCommand(PlatformModuleManagerServices services, String... args) {
	    // TODO: inject services
	    return new BuildCommand(null, args);
	}

    };

    /**
     * 
     */
    public static final PlatformModuleManagerCommand EXTRACT = new PlatformModuleManagerCommand() {

	@Override
	public String getName() {
	    return "extract";
	}

	@Override
	public String getDescription() {
	    return "runs the platform module extractor tool. for more information please run pmm extract -h";
	}

	@Override
	public Command constructCommand(PlatformModuleManagerServices services, String... args) {
	    return new ExtractCommand(args);
	}

    };

    public static final PlatformModuleManagerCommand DOCGEN = new PlatformModuleManagerCommand() {

	@Override
	public String getName() {
	    return "docgen";
	}

	@Override
	public String getDescription() {
	    return "generates docs/specs for a given platform module";
	}

	@Override
	public Command constructCommand(PlatformModuleManagerServices services, String... args) {
	    return new PMDocGenCommand(args);
	}
    };

    private static final Set<CLICommand> ALL_COMMANDS = new ImmutableSet.Builder<CLICommand>()
	    .add(HELP)
	    .add(EXTRACT)
	    .add(BUILD)
	    .add(DOCGEN)
	    .build();

    private static final OptionMessageProvider msgProvider = new OptionMessageProvider();

    @Override
    public boolean isHelpFlag() {
	return false;
    }

    @Override
    public String getFlag() {
	return null;
    }

    @Override
    public String getMessage() {
	return msgProvider.getMessage(Command.class, this.getName());
    }

    public static Set<CLICommand> getAllCommands() {
	return ALL_COMMANDS;
    }
}
