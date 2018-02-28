package com.gtnexus.pmm.pmdocgen;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.common.collect.ImmutableSet;
import com.gtnexus.pmm.AppXpressException;
import com.gtnexus.pmm.cli.option.CLICommandOption;
import com.gtnexus.pmm.cli.option.options.CustomerOption;
import com.gtnexus.pmm.cli.option.options.HelpOpt;
import com.gtnexus.pmm.cli.option.options.LocalDirOpt;
import com.gtnexus.pmm.cli.option.options.ModuleOption;
import com.gtnexus.pmm.cli.option.options.SelectOption;
import com.gtnexus.pmm.commons.command.PMMCommandInfo;
import com.gtnexus.pmm.context.ContextBasedCleanUp;
import com.gtnexus.pmm.context.PmmContext;
import com.gtnexus.pmm.platform.module.ModulePointer;
import com.gtnexus.pmm.platform.module.ModuleVO;
import com.gtnexus.pmm.platform.module.interpretation.PlatformModuleInterpreter;
import com.gtnexus.pmm.pmdocgen.cli.option.DocumentGeneratorOptions;

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
    public static final CLICommandOption selectOpt = new SelectOption();

    private final WorkbookRenderer renderer;

    private static final Set<CLICommandOption> options = new ImmutableSet.Builder<CLICommandOption>().add(helpOpt)
	    .add(customerOpt).add(moduleOpt).add(localDirOpt).add(selectOpt).build();

    public PlatformModuleDocumentGenerator() {
	this.renderer = new WorkbookRenderer();
    }

    public void generateDocs(PmmContext context) throws AppXpressException {
	attachCleanUpHook(context);
	ModulePointer pointer = ModulePointer.make(context.getOptMap());
	PlatformModuleInterpreter interp = new PlatformModuleInterpreter(pointer);
	ModuleVO vo = interp.interpret();
	XSSFWorkbook wb = renderer.render(vo);
	File file = new File(fileName(context.getOptMap()));
	try (FileOutputStream fileOut = new FileOutputStream(file)) {
	    wb.write(fileOut);
	} catch (IOException e) {
	    throw new AppXpressException("Failed to write documentation to file.", e);
	}
	System.out.println("Success! Documentation written to " + file.toPath().toAbsolutePath().toString());
    }

    private void attachCleanUpHook(PmmContext ctx) {
	Runtime.getRuntime().addShutdownHook(new Thread(new ContextBasedCleanUp(ctx)));
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
