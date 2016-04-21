package com.gtnexus.appxpress.commons.properties;

import com.gtnexus.appxpress.Exception.AppXpressException;
import com.gtnexus.appxpress.cli.asker.Asker;
import com.gtnexus.appxpress.cli.asker.SimpleAsker;
import com.gtnexus.appxpress.pmextractor.exception.PMExtractorException;

public class PropertiesPersister {

	private Asker<String> asker;
	private PMProperties properties;
	
	public PropertiesPersister(PMProperties properties) {
		this.asker = new SimpleAsker(System.in, System.out);
		this.properties = properties;
	}

	/**
	 * @param propPath
	 *            the path to the Properties file to be written to
	 * @throws PMExtractorException
	 *             when there is an IOException when writing to the properties
	 *             file at the propPath.
	 */
	public void presentSaveOption()
			throws AppXpressException {
		final String answer = askSaveQuestion();
		if (answer.equalsIgnoreCase("Y")) {
			saveProps();
		}
	}
	
	/**
	 * 
	 * @throws PMExtractorException
	 */
	private void saveProps() throws AppXpressException {
		properties.store();
	}
	
	/**
	 * 
	 * @return
	 */
	private String askSaveQuestion() {
		String answer = asker.ask("Save settings? [y/n]: ");
		while (!answer.equalsIgnoreCase("Y") && !answer.equalsIgnoreCase("N")) {
			answer = asker.ask(Asker.INVALID_INPUT);
		}
		return answer;
	}

}
