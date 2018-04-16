package com.gtnexus.pmm.app.common.properties;

import com.gtnexus.pmm.api.v100.PMProperties;
import com.gtnexus.pmm.api.v100.cli.prompt.Prompt;
import com.gtnexus.pmm.api.v100.cli.prompt.SimplePrompt;
import com.gtnexus.pmm.api.v100.command.SubCommandException;
import com.gtnexus.pmm.app.extract.exception.PMExtractorException;

public class PropertiesPersister {

    private Prompt<String> asker;
    private PMProperties properties;

    public PropertiesPersister(PMProperties properties) {
	this.asker = new SimplePrompt(System.in, System.out);
	this.properties = properties;
    }

    /**
     * @param propPath
     *            the path to the Properties file to be written to
     * @throws PMExtractorException
     *             when there is an IOException when writing to the properties file
     *             at the propPath.
     */
    public void presentSaveOption() throws SubCommandException {
	final String answer = askSaveQuestion();
	if (answer.equalsIgnoreCase("Y")) {
	    saveProps();
	}
    }

    /**
     * 
     * @throws PMExtractorException
     */
    private void saveProps() throws SubCommandException {
	properties.store();
    }

    /**
     * 
     * @return
     */
    private String askSaveQuestion() {
	String answer = asker.prompt("Save settings? [y/n]: ");
	while (!answer.equalsIgnoreCase("Y") && !answer.equalsIgnoreCase("N")) {
	    answer = asker.prompt(Prompt.INVALID_INPUT);
	}
	return answer;
    }

}
