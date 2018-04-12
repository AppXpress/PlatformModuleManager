package com.gtnexus.pmm.pmdocgen;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.common.collect.ImmutableSet;
import com.gtnexus.pmm.AppXpressException;
import com.gtnexus.pmm.api.v100.command.AbstractSubCommand;
import com.gtnexus.pmm.api.v100.command.SubCommandMarker;
import com.gtnexus.pmm.api.v100.service.PlatformModuleManagerServices;
import com.gtnexus.pmm.cli.option.CommandOption;
import com.gtnexus.pmm.cli.option.CommandOption.StandardOptions;
import com.gtnexus.pmm.commons.CommandOptionCompleter;
import com.gtnexus.pmm.platform.module.ModulePointer;
import com.gtnexus.pmm.platform.module.ModuleVO;
import com.gtnexus.pmm.platform.module.interpretation.PlatformModuleInterpreter;

/**
 * Generates documentation for the given platform module.
 * 
 * Currently only supporting excel, but is open to supporting other formats in
 * the future.
 * 
 * @author jdonovan
 */
@SubCommandMarker(
	name = "docgen",
	description = "generates docs/specs for a given platform module"
)
public class DocGenCommand extends AbstractSubCommand {

    private static final String NAME = "pmdocgen";

    private final WorkbookRenderer renderer;

    private static final Set<CommandOption> requiredOptions = new ImmutableSet.Builder<CommandOption>()
	    .add(StandardOptions.LOCAL_DIR)
	    .add(StandardOptions.CUSTOMER)
	    .add(StandardOptions.MODULE)
	    .build();

    public DocGenCommand(PlatformModuleManagerServices services, String... args) {
	super(services, args);
	this.renderer = new WorkbookRenderer();
    }

    @Override
    public void execute() throws AppXpressException {
	// attachCleanUpHook(context);
	Map<CommandOption, String> optionsMap = this.parse();
	optionsMap = new CommandOptionCompleter(getServices(), requiredOptions).complete(optionsMap);
	ModulePointer pointer = ModulePointer.make(optionsMap);
	PlatformModuleInterpreter interp = new PlatformModuleInterpreter(pointer);
	ModuleVO vo = interp.interpret();
	XSSFWorkbook wb = renderer.render(vo);
	File file = new File(fileName(optionsMap));
	try (FileOutputStream fileOut = new FileOutputStream(file)) {
	    wb.write(fileOut);
	} catch (IOException e) {
	    throw new AppXpressException("Failed to write documentation to file.", e);
	}
	System.out.println("Success! Documentation written to " + file.toPath().toAbsolutePath().toString());
    }

    @Override
    public String getName() {
	return NAME;
    }

    @Override
    public Set<CommandOption> getOptions() {
	return StandardOptions.set();
    }

    private String fileName(Map<CommandOption, String> optMap) {
	String fileNameTemplate = "%s-%s-TechnicalSpecification.xlsx";
	String customer = optMap.get(StandardOptions.CUSTOMER);
	String module = optMap.get(StandardOptions.MODULE);
	return String.format(fileNameTemplate, customer, module);
    }

}
