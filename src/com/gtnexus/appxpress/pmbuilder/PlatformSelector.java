package com.gtnexus.appxpress.pmbuilder;

import java.io.File;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Collection;

import com.gtnexus.appxpress.cli.asker.Asker;
import com.gtnexus.appxpress.cli.asker.BoundIntegerAsker;
import com.gtnexus.appxpress.cli.asker.SimpleAsker;

/**
 * 
 * @author jdonovan
 *
 */
public class PlatformSelector implements Select<File> {

	private final BoundIntegerAsker asker;

	private static final String messageTemplate = "Please select one of the following:\n";
	private static final String optionTemplate = "%s) %s";

	public PlatformSelector(InputStream in, PrintStream out) {
		this.asker = new BoundIntegerAsker(in, out);
	}

	public PlatformSelector(BoundIntegerAsker asker) {
		this.asker = asker;
	}

	public PlatformSelector(SimpleAsker asker) {
		this.asker = new BoundIntegerAsker(asker);
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public File select(Collection<File> choices) {
		File[] choiceArr = (File[]) choices.toArray();
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
		}
		return sb.toString();
	}

	/**
	 * 
	 * @return
	 */
	private Integer getSelectionFromUser(String message) {
		Integer selection = asker.ask(message);
		while (selection == null) {
			selection = asker.ask(Asker.INVALID_INPUT);
		}
		return selection - 1;
	}

}
