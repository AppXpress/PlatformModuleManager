package com.gtnexus.pmm.cli.command;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.gtnexus.pmm.PlatformModuleManagerServices;
import com.gtnexus.pmm.SubCommandMarker;
import com.gtnexus.pmm.commons.command.AbstractSubCommand;
import com.gtnexus.pmm.commons.command.Command;

public interface CLICommand {
    
    public String getName();

    public String getDescription();

    public String getFlag();

    public Command constructCommand(PlatformModuleManagerServices services, String... args);

    public static class Helper {
	public static CLICommand wrap(final SubCommandMarker marker, final Constructor<? extends AbstractSubCommand> constructor) {
	    return new CLICommand() {
		
		@Override
		public String getName() {
		    return marker.name();
		}

		@Override
		public String getFlag() {
		    return marker.flag();
		}

		@Override
		public String getDescription() {
		    return marker.description();
		}

		@Override
		public Command constructCommand(PlatformModuleManagerServices services, String... args) {
		    try {
			return constructor.newInstance(services, args);
		    } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
			    | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		    }
		}
	    };
	}
    }
}
