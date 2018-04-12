package com.gtnexus.pmm.pmbuilder;

import java.io.File;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.gtnexus.pmm.AppXpressException;
import com.gtnexus.pmm.api.v100.command.AbstractSubCommand;
import com.gtnexus.pmm.api.v100.command.SubCommandMarker;
import com.gtnexus.pmm.api.v100.service.PlatformModuleManagerServices;
import com.gtnexus.pmm.cli.option.CommandOption;
import com.gtnexus.pmm.cli.option.CommandOption.StandardOptions;
import com.gtnexus.pmm.commons.CommandOptionCompleter;
import com.gtnexus.pmm.commons.SubCommandHelpFormatter;
import com.gtnexus.pmm.pmbuilder.bundle.platform.BuildPrep;
import com.gtnexus.pmm.pmbuilder.bundle.platform.PlatformModuleBundler;
import com.gtnexus.pmm.pmbuilder.cli.PMBuilderVO;

@SubCommandMarker(
	name = BuildCommand.NAME, 
	description = "runs the platform module builder tool. for more information please run pmm build -h"
)
public class BuildCommand extends AbstractSubCommand {

    public static final String NAME = "build";
    
    private final Set<CommandOption> requiredOptions = new ImmutableSet.Builder<CommandOption>()
	    .add(StandardOptions.LOCAL_DIR)
	    .add(StandardOptions.CUSTOMER)
	    .add(StandardOptions.MODULE)
	    .build();

    public BuildCommand(PlatformModuleManagerServices services, String... args) {
	super(services, args);
    }

    @Override
    public String getName() {
	return NAME;
    }

    @Override
    public void execute() throws AppXpressException {
	Map<CommandOption, String> optionsMap = this.parse();
	if(optionsMap.containsKey(StandardOptions.HELP)) {
	    new SubCommandHelpFormatter(this).displayHelp();
	    return;
	}
	optionsMap = new CommandOptionCompleter(this.getServices(), requiredOptions).complete(optionsMap);
	PMBuilderVO vo = genValueObj(optionsMap);
	BuildPrep prep = new BuildPrep(
		this.getServices(),
		this.getServices().getEnvironmentService().getLibPath());
	PlatformModuleBundler bundler = new PlatformModuleBundler(vo.getRootFile());
	try {
	    prep.prepare(vo);
	    bundler.bundle(vo.getWorkingDir());
	} catch (AppXpressException e) {
	    throw new AppXpressException("Failed to build module.", e);
	}
    }

    private PMBuilderVO genValueObj(Map<CommandOption, String> optMap) {
	Path localDir = this.getServices()
		.getEnvironmentService()
		.getLocalDir();
	String customer = optMap.get(CommandOption.StandardOptions.CUSTOMER);
	String module = optMap.get(CommandOption.StandardOptions.MODULE);
	File rootFile = localDir.resolve(customer)
		.resolve(module)
		.toFile();
	return new PMBuilderVO(localDir.toFile(), rootFile);
    }

}
