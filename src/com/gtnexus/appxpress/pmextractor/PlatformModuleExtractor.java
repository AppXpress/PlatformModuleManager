package com.gtnexus.appxpress.pmextractor;

import java.io.IOException;

import com.gtnexus.appxpress.AppXpressException;
import com.gtnexus.appxpress.cli.option.AppXpressOption;
import com.gtnexus.appxpress.commons.ApplicationInfo;
import com.gtnexus.appxpress.context.AppXpressContext;
import com.gtnexus.appxpress.context.ContextBasedCleanUp;
import com.gtnexus.appxpress.context.ContextFactory;
import com.gtnexus.appxpress.pmextractor.cli.ExtractorOption;
import com.gtnexus.appxpress.pmextractor.gitmap.GitMapper;
import com.gtnexus.appxpress.pmextractor.gitmap.Mapper;

/**
 * 
 * @author jdonovan
 *
 */
public class PlatformModuleExtractor implements ApplicationInfo {

	public static void main(String args[]) throws IOException {
		ContextFactory factory = new ContextFactory();
		try {
			PlatformModuleExtractor extractor = new PlatformModuleExtractor();
			AppXpressContext<ExtractorOption> context = factory.creatContext(extractor, args);
			extractor.extract(context);
		} catch (AppXpressException e) {
			System.err.println(e.getAppXpressMessage());
		}
	}

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

	@SuppressWarnings("unchecked")
	@Override
	public <M extends Enum<M> & AppXpressOption> Class<M> getContextType() {
		return (Class<M>) ExtractorOption.class;
	}

}
