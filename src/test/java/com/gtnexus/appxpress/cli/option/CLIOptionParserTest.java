package com.gtnexus.appxpress.cli.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.EnumSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableSet;
import com.gtnexus.appxpress.exception.AppXpressException;
import com.gtnexus.appxpress.pmextractor.cli.DummyOption;

public class CLIOptionParserTest {

    CLIOptionParser<DummyOption> cli;
    String appName = "ParserTest";
    Set<DummyOption> optSet = new ImmutableSet.Builder<DummyOption>().add(DummyOption.DUMMY).build();

    @Before
    public void setUp() {
	String[] args = { "-Dummy" };
	cli = CLIOptionParser.createParser(optSet, args);
    }

    @Test(expected = NullPointerException.class)
    public void testWithNullArgs() {
	new CLIOptionParser<>(null, null, null);
    }

    @Test
    public void testParse() throws Exception {
	ParsedOptions<DummyOption> parsedOpts = cli.parse();
	assertTrue(parsedOpts.hasOption(DummyOption.DUMMY));
    }

    @Test(expected = AppXpressException.class)
    public void testParseWithInvalidOpt() throws Exception {
	String[] args = { "-invalidOpt" };
	CLIOptionParser<DummyOption> cli2 = CLIOptionParser.createParser(optSet, args);
	cli2.parse();
    }

    @Test
    public void testGetCliOptionSet() throws Exception {
	assertEquals(EnumSet.allOf(DummyOption.class), cli.getCliOptionSet());
    }

}
