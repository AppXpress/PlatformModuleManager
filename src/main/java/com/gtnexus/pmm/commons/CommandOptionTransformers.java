package com.gtnexus.pmm.commons;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import com.gtnexus.pmm.cli.option.CommandOption;


public class CommandOptionTransformers {

    public static Option transformToOption(CommandOption opt) {
	return 	Option
		.builder(opt.getFlag())
		.longOpt(opt.getLongName())
		.type(opt.getType())
		.desc(opt.getDescription())
		.hasArg(opt.hasArg())
		.required(false)
		.build();
    }

    public static Options transformToOptions(Iterable<CommandOption> opts) {
	Options options = new Options();
	for(CommandOption opt : opts) {
	   options.addOption(transformToOption(opt)); 
	}
	return options;
    }
    
}
