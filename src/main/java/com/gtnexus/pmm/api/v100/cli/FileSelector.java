package com.gtnexus.pmm.api.v100.cli;

import java.io.File;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Collection;

import com.gtnexus.pmm.api.v100.cli.prompt.BoundIntegerAsker;
import com.gtnexus.pmm.api.v100.cli.prompt.Prompt;
import com.gtnexus.pmm.api.v100.cli.prompt.SimplePrompt;

/**
 * 
 * @author jdonovan
 *
 */
public class FileSelector implements Select<File> {

    private final BoundIntegerAsker asker;

    private static final String messageTemplate = "Please select one of the following:\n";
    private static final String optionTemplate = "%s) %s";

    public FileSelector(InputStream in, PrintStream out) {
	this.asker = new BoundIntegerAsker(in, out);
    }

    public FileSelector(BoundIntegerAsker asker) {
	this.asker = asker;
    }

    public FileSelector(SimplePrompt asker) {
	this.asker = new BoundIntegerAsker(asker);
    }

    /**
     * 
     * @return
     */
    @Override
    public File select(Collection<File> choices) {
	File[] choiceArr = choices.toArray(new File[choices.size()]);
	enusreAppropriateBounds(choiceArr);
	String message = createMessage(choiceArr);
	Integer selection = getSelectionFromUser(message);
	return choiceArr[selection];
    }

    private void enusreAppropriateBounds(File[] choiceArr) {
	asker.setLowerBound(1);
	asker.setUpperBound(choiceArr.length);
    }

    private String createMessage(File[] choices) {
	StringBuilder sb = new StringBuilder();
	sb.append(messageTemplate);
	int i = 0;
	for (File f : choices) {
	    sb.append("\n");
	    sb.append(String.format(optionTemplate, i + 1, f));
	    i++;
	}
	return sb.toString();
    }

    /**
     * 
     * @return
     */
    private Integer getSelectionFromUser(String message) {
	Integer selection = asker.prompt(message);
	while (selection == null) {
	    selection = asker.prompt(Prompt.INVALID_INPUT);
	}
	return selection - 1;
    }

}
