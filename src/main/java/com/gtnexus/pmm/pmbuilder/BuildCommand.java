package com.gtnexus.pmm.pmbuilder;

import java.util.Map;
import java.util.Set;

import com.gtnexus.pmm.AppXpressException;
import com.gtnexus.pmm.PlatformModuleManagerServices;
import com.gtnexus.pmm.SubCommandMarker;
import com.gtnexus.pmm.cli.option.CLICommandOption;
import com.gtnexus.pmm.commons.command.AbstractSubCommand;
import com.gtnexus.pmm.pmbuilder.bundle.platform.BuildPrep;
import com.gtnexus.pmm.pmbuilder.bundle.platform.PlatformModuleBundler;
import com.gtnexus.pmm.pmbuilder.cli.BuilderOption;
import com.gtnexus.pmm.pmbuilder.cli.PMBuilderVO;

@SubCommandMarker(
	name = "build", 
	flag = "b", 
	description = "build all the things!")
public class BuildCommand extends AbstractSubCommand {

    private static final String NAME = "pmbuilder";

    public BuildCommand(PlatformModuleManagerServices services, String... args) {
	super(services, args);
    }

    @Override
    public String getName() {
	return NAME;
    }

    @Override
    public String getHelpHeader() {
	return "";
    }

    @Override
    public String getHelpFooter() {
	return "";
    }

    @Override
    public Set<CLICommandOption> getOptions() {
	return BuilderOption.getAllOptions();
    }

    @Override
    public void execute() throws AppXpressException {
	Map<CLICommandOption, String> optionsMap = this.parse().getOptionsMap();
	optionsMap.put(BuilderOption.LOCAL_DIR, this.getServices().getEnvironmentService().getLocalDir().toString());
	PMBuilderVO vo = new PMBuilderVO(optionsMap);
	BuildPrep prep = new BuildPrep(
		this.getServices().getFileService(),
		this.getServices().getTemporaryResourceService(),
		this.getServices().getEnvironmentService().getLibPath());
	PlatformModuleBundler bundler = new PlatformModuleBundler(vo.getRootFile());
	try {
	    prep.prepare(vo);
	    bundler.bundle(vo.getWorkingDir());
	} catch (AppXpressException e) {
	    throw new AppXpressException("Failed to build module.", e);
	}
    }
    
}
