package com.gtnexus.appxpress.cli.option;

import static org.junit.Assert.fail;

import java.util.EnumSet;
import java.util.HashMap;

import org.apache.commons.cli.Options;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gtnexus.appxpress.commons.AppXpressDirResolver;
import com.gtnexus.appxpress.commons.CommandInformation;
import com.gtnexus.appxpress.commons.PMProperties;
import com.gtnexus.appxpress.commons.SimpleShutdown;
import com.gtnexus.appxpress.commons.exception.AppXpressException;
import com.gtnexus.appxpress.pmbuilder.PlatformSelector;
import com.gtnexus.appxpress.pmbuilder.cli.BuilderOption;

@RunWith(MockitoJUnitRunner.class)
public class BuilderOptionInterpreterTest {

	@Mock
	CommandInformation app;
	@Mock
	SimpleShutdown shutdown;
	@Mock
	PMProperties properties;
	@Mock
	PlatformSelector selector;

	@Mock
	AppXpressDirResolver resolver;

	BuilderOptionInterpreter interpreter;
	HashMap<BuilderOption, String> optMap = new HashMap<>();

	@Before
	public void setup() {

	}

	@Test(expected = NullPointerException.class)
	public void testPerformCustomInterpretationWithNull() throws Exception {
		interpreter.performCustomInterpretation(null);
	}

	@Test
	public void testPerformCustomInterpretationWithoutSPFlag()
			throws AppXpressException {
		ParsedOptions<BuilderOption> parsedOptions = new ParsedOptions<>(
				new Options(), new HashMap<BuilderOption, String>(),
				EnumSet.allOf(BuilderOption.class));
		BuilderOptionInterpreter interpeter = new BuilderOptionInterpreter(app,
				shutdown, parsedOptions, properties, selector, resolver);
		fail("unimplemented");
	}

	@Test
	public void testPerformCustomInterpretationWithSPFlag()
			throws AppXpressException {
		fail("unimplemented");
	}
}
