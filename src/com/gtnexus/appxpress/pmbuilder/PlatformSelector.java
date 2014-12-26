package com.gtnexus.appxpress.pmbuilder;

import java.io.File;
import java.io.InputStream;
import java.io.PrintStream;

import com.gtnexus.appxpress.cli.Asker;
import com.gtnexus.appxpress.cli.BoundIntegerAsker;
/**
 * 
 * @author jdonovan
 *
 */
public class PlatformSelector {

	private final Asker<Integer> asker;
	private final String message;
	private final File[] candidates;

	public PlatformSelector(InputStream in, PrintStream out,
			File[] candidates, String message) {
		this.asker = new BoundIntegerAsker(in, out, 0, candidates.length);
		this.candidates = candidates;
		this.message = message;
	}

	/**
	 * 
	 * @return
	 */
	public String select() {
		Integer selection = getSelectionFromUser();
		return candidates[selection].getName();
	}

	/**
	 * 
	 * @return
	 */
	public Integer getSelectionFromUser() {
		Integer selection = asker.ask(message);
		while (selection == null) {
			selection = asker.ask(Asker.INVALID_INPUT);
		}
		return selection + 1;
	}

}
