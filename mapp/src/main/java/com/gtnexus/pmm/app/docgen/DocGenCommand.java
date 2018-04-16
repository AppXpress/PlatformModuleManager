package com.gtnexus.pmm.app.docgen;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.common.collect.ImmutableSet;
import com.gtnexus.pmm.api.v100.cli.option.CommandOption;
import com.gtnexus.pmm.api.v100.cli.option.CommandOption.StandardOptions;
import com.gtnexus.pmm.api.v100.command.AbstractSubCommand;
import com.gtnexus.pmm.api.v100.command.SubCommandException;
import com.gtnexus.pmm.api.v100.command.SubCommandMarker;
import com.gtnexus.pmm.api.v100.service.PlatformModuleManagerServices;
import com.gtnexus.pmm.app.common.CommandOptionCompleter;
import com.gtnexus.pmm.app.common.SubCommandHelpFormatter;
import com.gtnexus.pmm.app.platform.module.ModulePointer;
import com.gtnexus.pmm.app.platform.module.ModuleVO;
import com.gtnexus.pmm.app.platform.module.interpretation.PlatformModuleInterpreter;

/**
 * Generates documentation for the given platform module.
 * 
 * Currently only supporting excel, but is open to supporting other formats in
 * the future.
 * 
 * @author jdonovan
 */
@SubCommandMarker(
	name = DocGenCommand.NAME,
	description = "generates docs/specs for a given platform module"
)
public class DocGenCommand extends AbstractSubCommand {

    public static final String NAME = "docgen";

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
    public void execute() throws SubCommandException {
	// attachCleanUpHook(context);
	Map<CommandOption, String> optionsMap = this.parse();
	if(optionsMap.containsKey(StandardOptions.HELP)) {
	    new SubCommandHelpFormatter(this).displayHelp();
	    return;
	}
	optionsMap = new CommandOptionCompleter(getServices(), requiredOptions).complete(optionsMap);
	ModulePointer pointer = ModulePointer.make(optionsMap);
	PlatformModuleInterpreter interp = new PlatformModuleInterpreter(pointer);
	ModuleVO vo = interp.interpret();
	XSSFWorkbook wb = renderer.render(vo);
	File file = new File(fileName(optionsMap));
	try (FileOutputStream fileOut = new FileOutputStream(file)) {
	    wb.write(fileOut);
	} catch (IOException e) {
	    throw new SubCommandException("Failed to write documentation to file.", e);
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
