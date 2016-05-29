package com.gtnexus.appxpress.pmdocgen;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.common.collect.ImmutableSet;
import com.gtnexus.appxpress.cli.option.CLICommandOption;
import com.gtnexus.appxpress.cli.option.options.CustomerOption;
import com.gtnexus.appxpress.cli.option.options.HelpOpt;
import com.gtnexus.appxpress.cli.option.options.LocalDirOpt;
import com.gtnexus.appxpress.cli.option.options.ModuleOption;
import com.gtnexus.appxpress.commons.command.PMMCommandInfo;
import com.gtnexus.appxpress.context.AppXpressContext;
import com.gtnexus.appxpress.context.ContextBasedCleanUp;
import com.gtnexus.appxpress.exception.AppXpressException;
import com.gtnexus.appxpress.platform.module.ModulePointer;
import com.gtnexus.appxpress.platform.module.ModuleVO;
import com.gtnexus.appxpress.platform.module.interpretation.PlatformModuleInterpreter;
import com.gtnexus.appxpress.pmdocgen.cli.option.DocumentGeneratorOptions;

/**
 * Generates documentation for the given platform module.
 * 
 * Currently only supporting excel, but is open to supporting other formats in
 * the future.
 * 
 * @author jdonovan
 */
public class PlatformModuleDocumentGenerator implements PMMCommandInfo {

	private static final String NAME = "pmdocgen";

	public static final CLICommandOption helpOpt = new HelpOpt();
	public static final CLICommandOption customerOpt = new CustomerOption();
	public static final CLICommandOption moduleOpt = new ModuleOption();
	public static final CLICommandOption localDirOpt = new LocalDirOpt();

	private final WorkbookRenderer renderer;

	private static final Set<CLICommandOption> options = new ImmutableSet.Builder<CLICommandOption>()
			.add(helpOpt)
			.add(customerOpt)
			.add(moduleOpt)
			.add(localDirOpt).build();

	public PlatformModuleDocumentGenerator() {
		this.renderer = new WorkbookRenderer();
	}

	public void generateDocs(AppXpressContext<CLICommandOption> context) throws AppXpressException {
		attachCleanUpHook(context);
		ModulePointer pointer = ModulePointer.make(context.getOptMap());
		PlatformModuleInterpreter interp = new PlatformModuleInterpreter(pointer);
		ModuleVO vo = interp.interpret();
		XSSFWorkbook wb = renderer.render(vo);
		String fileName = fileName(context.getOptMap());
		try (FileOutputStream fileOut = new FileOutputStream(fileName)) {
			wb.write(fileOut);
		} catch (IOException e) {
			throw new AppXpressException("Failed to write documentation to file.", e);
		}
	}
	
	private void attachCleanUpHook(AppXpressContext<CLICommandOption> ctx) {
		Runtime.getRuntime().addShutdownHook(
				new Thread(new ContextBasedCleanUp<>(ctx)));
	}

	private String fileName(Map<CLICommandOption, String> optMap) {
		String fileNameTemplate = "%s-%s-TechnicalSpecification.xlsx";
		return String.format(fileNameTemplate, optMap.get(customerOpt), optMap.get(moduleOpt));
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
	public Class<?> getContextType() {
		return DocumentGeneratorOptions.class;
	}

	@Override
	public Set<CLICommandOption> getOptions() {
		return options;
	}

}
