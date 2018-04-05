package com.gtnexus.appxpress.cli.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.EnumSet;
import java.util.Set;

import org.junit.Test;

import com.google.common.collect.ImmutableSet;
import com.gtnexus.appxpress.pmextractor.cli.DummyOption;
import com.gtnexus.pmm.AppXpressException;
import com.gtnexus.pmm.cli.option.CLICommandOption;
import com.gtnexus.pmm.cli.option.CLICommandOptionParser;
import com.gtnexus.pmm.cli.option.ParsedOptions;

public class CLICommandOptionParserTest {

    private final Set<CLICommandOption> optSet = new ImmutableSet.Builder<CLICommandOption>().add(DummyOption.DUMMY).build();
    private final CLICommandOptionParser parser = CLICommandOptionParser.createParser(optSet);

    @Test(expected = NullPointerException.class)
    public void testWithNullArgs() {
	new CLICommandOptionParser(null, null);
    }

    @Test
    public void testParse() throws Exception {
	ParsedOptions parsedOpts = parser.parse(new String[] {"-Dummy"});
	assertTrue(parsedOpts.hasOption(DummyOption.DUMMY));
    }

    @Test(expected = AppXpressException.class)
    public void testParseWithInvalidOpt() throws Exception {
	parser.parse(new String[] {"-invalidOpt"});
    }

    @Test
    public void testGetCliOptionSet() throws Exception {
	assertEquals(EnumSet.allOf(DummyOption.class), parser.getCliOptionSet());
    }

}
