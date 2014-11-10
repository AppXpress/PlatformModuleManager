package com.gtnexus.appxpress.cli;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.gtnexus.appxpress.pmextractor.cli.DummyOption;
import com.gtnexus.appxpress.pmextractor.exception.PMExtractorException;

public class CommandLineInterfaceParserTest {

	@Test(expected = NullPointerException.class)
	public void testNullArgs() {
		new CommandLineInterfaceParser<>(null, null);
	}
	
	@Test
	public void testHasOption() throws PMExtractorException {
		String[] ha = {"-Dummy"};
		Set<DummyOption> optSet = new HashSet<>();
		CLIOption opt = new DummyOption();
		optSet.add(new DummyOption());
		CommandLineInterfaceParser<DummyOption> cli = new CommandLineInterfaceParser<>(
				ha , optSet);
		assertFalse(cli.hasOption(opt));
		cli.parseCommandLine();
		assertTrue(cli.hasOption(opt));
	}

}
