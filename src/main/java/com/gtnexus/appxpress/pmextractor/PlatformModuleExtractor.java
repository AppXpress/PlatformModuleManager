package com.gtnexus.appxpress.pmextractor;

import java.util.Set;

import com.gtnexus.appxpress.AppXpressException;
import com.gtnexus.appxpress.cli.option.AppXpressOption;
import com.gtnexus.appxpress.commons.ApplicationInfo;
import com.gtnexus.appxpress.context.AppXpressContext;
import com.gtnexus.appxpress.context.ContextBasedCleanUp;
import com.gtnexus.appxpress.pmextractor.cli.ExtractorOption;
import com.gtnexus.appxpress.pmextractor.gitmap.GitMapper;
import com.gtnexus.appxpress.pmextractor.gitmap.Mapper;

/**
 * 
 * @author jdonovan
 *
 */
public class PlatformModuleExtractor implements ApplicationInfo {

	private static final String NAME = "pmextractor";

	public PlatformModuleExtractor() {}
	
	public void extract(AppXpressContext<ExtractorOption> context)
			throws AppXpressException {
		attachCleanUpHook(context);
		Mapper tool = GitMapper.createMapper(context);
		try {
			tool.doMapping();
			System.out.println("Success!");
		} catch (AppXpressException e) {
			context.setTerminatedRegulary(false);
			throw new AppXpressException("Failure when running pmextractor.", e);
		}
	}
	
	private void attachCleanUpHook(AppXpressContext<ExtractorOption> ctx) {
		Runtime.getRuntime().addShutdownHook(
				new Thread(new ContextBasedCleanUp<>(ctx)));
	}
	
	@Override
	public String getAppName() {
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
	public  Class<?> getContextType() {
		return ExtractorOption.class;
	}

	@Override
	public <M extends AppXpressOption> Set<M> getOptions() {
		// TODO Auto-generated method stub
		return null;
	}

}
