package com.gtnexus.pmm.build;

import java.io.File;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.gtnexus.pmm.api.v100.cli.option.CommandOption;
import com.gtnexus.pmm.api.v100.cli.option.CommandOption.StandardOptions;
import com.gtnexus.pmm.api.v100.command.AbstractSubCommand;
import com.gtnexus.pmm.api.v100.command.SubCommandException;
import com.gtnexus.pmm.api.v100.command.SubCommandMarker;
import com.gtnexus.pmm.api.v100.service.PlatformModuleManagerServices;
import com.gtnexus.pmm.build.bundle.platform.BuildPrep;
import com.gtnexus.pmm.build.bundle.platform.PlatformModuleBundler;
import com.gtnexus.pmm.build.cli.PMBuilderVO;
import com.gtnexus.pmm.common.CommandOptionCompleter;
import com.gtnexus.pmm.common.SubCommandHelpFormatter;

@SubCommandMarker(
	name = BuildCommand.NAME, 
	description = BuildCommand.DESCRIPTION
)
public class BuildCommand extends AbstractSubCommand {

    public static final String NAME = "build";
    public static final String DESCRIPTION = "runs the platform module builder tool. for more information please run pmm build -h";
    
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
    public void execute() throws SubCommandException {
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
	} catch (SubCommandException e) {
	    throw new SubCommandException("Failed to build module.", e);
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
