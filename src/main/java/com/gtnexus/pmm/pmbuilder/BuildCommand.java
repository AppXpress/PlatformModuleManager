package com.gtnexus.pmm.pmbuilder;

import java.io.File;
import java.nio.file.Path;
import java.util.Map;
import java.util.Map.Entry;

import com.gtnexus.pmm.AppXpressException;
import com.gtnexus.pmm.SubCommandMarker;
import com.gtnexus.pmm.api.v100.command.AbstractSubCommand;
import com.gtnexus.pmm.api.v100.service.PlatformModuleManagerServices;
import com.gtnexus.pmm.cli.OptsAndPropConsolidator;
import com.gtnexus.pmm.cli.option.CommandOption;
import com.gtnexus.pmm.cli.option.ParsedOptions;
import com.gtnexus.pmm.pmbuilder.bundle.platform.BuildPrep;
import com.gtnexus.pmm.pmbuilder.bundle.platform.PlatformModuleBundler;
import com.gtnexus.pmm.pmbuilder.cli.PMBuilderVO;

@SubCommandMarker(name = "build", description = "runs the platform module builder tool. for more information please run pmm build -h")
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
    protected ParsedOptions parse() throws AppXpressException {
	ParsedOptions parsedOpts = super.parse();
	OptsAndPropConsolidator consolidator = new OptsAndPropConsolidator(parsedOpts.getOptionsMap(),
		this.getOptions(), this.getServices().getEnvironmentService().getProperties());
	Map<CommandOption, String> consolidated = consolidator.consolidate();
	// TODO: hacky..parsedopts purpose is not clear. Seems to just get in the way
	for (Entry<CommandOption, String> entry : consolidated.entrySet()) {
	    parsedOpts.put(entry.getKey(), entry.getValue());
	}
	return parsedOpts;
    }

    @Override
    public void execute() throws AppXpressException {
	Map<CommandOption, String> optionsMap = this.parse().getOptionsMap();
	PMBuilderVO vo = genValueObj(optionsMap);
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
