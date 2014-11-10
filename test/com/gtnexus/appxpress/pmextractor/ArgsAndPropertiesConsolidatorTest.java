package com.gtnexus.appxpress.pmextractor;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.gtnexus.appxpress.NullOutputStream;
import com.gtnexus.appxpress.pmextractor.cli.ArgsAndPropertiesConsolidator;
import com.gtnexus.appxpress.pmextractor.cli.ExtractorOption;

/**
 * Created by jjdonov on 9/29/14.
 */
public class ArgsAndPropertiesConsolidatorTest {

	private static Set<ExtractorOption> optSet;
	private static Map<ExtractorOption, String> mandatoryFieldsAndVals;
	private static Set<ExtractorOption> allNonOmmitable;

	@BeforeClass
	public static void  setup() {
		optSet = EnumSet.allOf(ExtractorOption.class);
		
	}
	
	@Before
	public void before() {
		mandatoryFieldsAndVals = new HashMap<>();
		mandatoryFieldsAndVals.put(ExtractorOption.PLATFORM_ZIP, "arg_myZip");
		mandatoryFieldsAndVals.put(ExtractorOption.LOCAL_DIR, "arg_myLocalDir");
		mandatoryFieldsAndVals.put(ExtractorOption.CUSTOMER, "arg_myCustomer");
		mandatoryFieldsAndVals.put(ExtractorOption.PLATFORM, "arg_myPlatform");
		allNonOmmitable =  EnumSet.allOf(ExtractorOption.class);
		allNonOmmitable.remove(ExtractorOption.HELP);
	}

	@Test
	public void testWithArgsAndNullProps() {
		Map<ExtractorOption, String> args = mandatoryFieldsAndVals;
		Properties properties = new Properties();
		ArgsAndPropertiesConsolidator<ExtractorOption> consolidator = new ArgsAndPropertiesConsolidator<>(
				args, optSet, properties, inputStreamFrom(), new PrintStream(new NullOutputStream()));
		Map<ExtractorOption, String> consolidated = consolidator.consolidate();
		for (ExtractorOption option : allNonOmmitable) {
			assertTrue("Option value was " + consolidated.get(option), 
					consolidated.get(option).startsWith("arg_") || 
					consolidated.get(option).equals("N"));
		}
	}

	@Test
	public void testWithArgsAndProps() {
		mandatoryFieldsAndVals.remove(ExtractorOption.LOCAL_DIR);
		mandatoryFieldsAndVals.remove(ExtractorOption.PLATFORM);
		Map<ExtractorOption, String> args = mandatoryFieldsAndVals;
		Properties properties = new Properties();
		properties.put(ExtractorOption.LOCAL_DIR.toString(), "prop_local_dir");
		properties.put(ExtractorOption.PLATFORM.toString(), "prop_platform");
		ArgsAndPropertiesConsolidator<ExtractorOption> consolidator = new ArgsAndPropertiesConsolidator<>(
				args, optSet, properties, inputStreamFrom(), new PrintStream(
						new NullOutputStream()));
		Map<ExtractorOption, String> consolidated = consolidator.consolidate();
		for (ExtractorOption option : allNonOmmitable) {
			if (option.equals(ExtractorOption.LOCAL_DIR)
					|| option.equals(ExtractorOption.PLATFORM)) {
				assertTrue(consolidated.get(option).startsWith("prop_"));
			} else {
				assertTrue(consolidated.get(option)
						+ " does not start with arg_.", 
						consolidated.get(option).startsWith("arg_") ||
						consolidated.get(option).equals("N"));
			}
		}
	}

	@Test
	public void testWithArgsPropsAndInputStream() {
		mandatoryFieldsAndVals.remove(ExtractorOption.LOCAL_DIR);
		Map<ExtractorOption, String> args = mandatoryFieldsAndVals;
		Properties properties = new Properties();
		properties.put(ExtractorOption.PLATFORM.toString(), "prop_platform");
		ArgsAndPropertiesConsolidator<ExtractorOption> consolidator = new ArgsAndPropertiesConsolidator<>(
				args, optSet, properties, inputStreamFrom("some_local_dir", "Y"),
				new PrintStream(new NullOutputStream()));
		Map<ExtractorOption, String> consolidated = consolidator.consolidate();
		assertTrue(consolidated.get(ExtractorOption.LOCAL_DIR).equals(
				"some_local_dir"));
	}

	@Test
	public void testDefaultValForOptional() {
		Map<ExtractorOption, String> args = mandatoryFieldsAndVals;
		Properties properties = new Properties();
		properties.put(ExtractorOption.PLATFORM.toString(), "prop_platform");
		ArgsAndPropertiesConsolidator<ExtractorOption> consolidator = new ArgsAndPropertiesConsolidator<>(
				args, optSet, properties, inputStreamFrom("some_local_dir", "Y"),
				new PrintStream(new NullOutputStream()));
		Map<ExtractorOption, String> consolidated = consolidator.consolidate();
		assertTrue(consolidated.get(ExtractorOption.OVERWRITE_FEF).equals("N"));
	}

	/**
	 * Creates an InputStream from strings, to fake i/o.
	 * 
	 * @param strings
	 * @return
	 */
	private InputStream inputStreamFrom(String... strings) {
		String separator = System.getProperty("line.separator");
		StringBuilder sb = new StringBuilder();
		for (String s : strings) {
			sb.append(s.replaceAll(separator, " ").trim()).append(separator);
		}
		return new ByteArrayInputStream(sb.toString().getBytes());
	}

}
