package com.gtnexus.appxpress.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.cli.Options;
import org.junit.Test;

import com.gtnexus.appxpress.cli.option.CLIOption;
import com.gtnexus.appxpress.cli.option.CommandLineInterfaceParser;
import com.gtnexus.appxpress.pmextractor.cli.DummyOption;
import com.gtnexus.appxpress.pmextractor.exception.PMExtractorException;

public class CommandLineInterfaceParserTest {

	@Test(expected = NullPointerException.class)
	public void testNullArgs() {
		new CommandLineInterfaceParser<>(null, null, null, null);
	}

	@Test
	public void testHasOption() throws PMExtractorException {
		String[] ha = { "-Dummy" };
		Set<DummyOption> optSet = new HashSet<>();
		CLIOption opt = new DummyOption();
		optSet.add(new DummyOption());
		CommandLineInterfaceParser<DummyOption> cli = new CommandLineInterfaceParser<>(
				"ParserTest", ha, optSet, new Options());
		assertFalse(cli.hasOption(opt));
		cli.parse();
		assertTrue(cli.hasOption(opt));
	}

}
