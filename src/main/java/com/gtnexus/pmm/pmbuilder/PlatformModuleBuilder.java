package com.gtnexus.pmm.pmbuilder;

import com.gtnexus.pmm.AppXpressException;
import com.gtnexus.pmm.PlatformModuleManagerServices;
import com.gtnexus.pmm.cli.option.ParsedOptions;
import com.gtnexus.pmm.context.ContextBasedCleanUp;
import com.gtnexus.pmm.context.PmmContext;
import com.gtnexus.pmm.pmbuilder.bundle.platform.BuildPrep;
import com.gtnexus.pmm.pmbuilder.bundle.platform.PlatformModuleBundler;
import com.gtnexus.pmm.pmbuilder.cli.PMBuilderVO;

/**
 * This executable does the following things in order with the end goal to
 * create an importable .zip file.
 * 
 * <ol>
 * <li>1) Scans the javascripts in the platform module for the <b>!import
 * symbol</b>. If found, automatically imports indicated scripts from lib/ into
 * correct folder.</li>
 * <li>2) Makes sure each of the custom object design xml's contain the correct
 * scriptingFeature tag, therefore ensuring that the platform module's scripts
 * will import correctly.</li>
 * <li>3) Maps the local git structure to the folder structure required to
 * become importable by GTNexus platform. This requires correct folder names and
 * bundling javascript file into zip files when necessary.</li>
 * <li>4) Zips up correctly mapped file structure into a zip file, ready to
 * import into GTNexus system</li>
 * </ol>
 *
 * @author Andrew Reynolds
 * @author john donovan
 * @version 1.0
 * @date 8-27-2014 GT Nexus
 */
@Deprecated
public class PlatformModuleBuilder /*implements PMMCommandInfo*/ {

    public void build(PmmContext context) throws AppXpressException {
	attachCleanUpHook(context);
	PMBuilderVO vo = new PMBuilderVO(context.getOptMap());
	BuildPrep prep = new BuildPrep(context.getTempResourceHolder(), context.getLibPath());
	PlatformModuleBundler bundler = new PlatformModuleBundler(vo.getRootFile());
	try {
	    prep.prepare(vo);
	    bundler.bundle(vo.getWorkingDir());
	} catch (AppXpressException e) {
	    context.setTerminatedRegulary(false);
	    throw new AppXpressException("Failed to build module.", e);
	}
    }
    
    private void attachCleanUpHook(PmmContext ctx) {
	Runtime.getRuntime().addShutdownHook(new Thread(new ContextBasedCleanUp(ctx)));
    }

}
