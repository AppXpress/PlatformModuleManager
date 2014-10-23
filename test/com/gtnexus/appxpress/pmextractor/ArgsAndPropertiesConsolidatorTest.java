package com.gtnexus.appxpress.pmextractor;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import com.gtnexus.appxpress.NullOutputStream;

/**
 * Created by jjdonov on 9/29/14.
 */
public class ArgsAndPropertiesConsolidatorTest {

	private static Set<ExtractorOption> optSet;

	@BeforeClass
	public static void  setup() {
		optSet = EnumSet.allOf(ExtractorOption.class);
	}

	@Test
	public void testWithArgsAndNullProps() {
		Map<ExtractorOption, String> args = Collections.emptyMap();
		Properties properties = new Properties();
		ArgsAndPropertiesConsolidator<ExtractorOption> consolidator = new ArgsAndPropertiesConsolidator<>(
				args, optSet, properties, inputStreamFrom(), new PrintStream(new NullOutputStream()));
		Map<ExtractorOption, String> consolidated = consolidator.consolidate();
		for (ExtractorOption option : EnumSet.allOf(ExtractorOption.class)) {
			assertTrue(consolidated.get(option).startsWith("arg_"));
		}
	}

	@Test
	public void testWithArgsAndProps() {
		Map<ExtractorOption, String> args = Collections.emptyMap();
		Properties properties = new Properties();
		properties.put(ExtractorOption.LOCAL_DIR.toString(), "prop_local_dir");
		properties.put(ExtractorOption.PLATFORM.toString(), "prop_platform");
		ArgsAndPropertiesConsolidator<ExtractorOption> consolidator = new ArgsAndPropertiesConsolidator<>(
				args, optSet, properties, inputStreamFrom(), new PrintStream(
						new NullOutputStream()));
		Map<ExtractorOption, String> consolidated = consolidator.consolidate();
		for (ExtractorOption option : EnumSet.allOf(ExtractorOption.class)) {
			if (option.equals(ExtractorOption.LOCAL_DIR)
					|| option.equals(ExtractorOption.PLATFORM)) {
				assertTrue(consolidated.get(option).startsWith("prop_"));
			} else {
				assertTrue(consolidated.get(option)
						+ " does not start with arg_.", consolidated
						.get(option).startsWith("arg_"));
			}
		}
	}

	@Test
	public void testWithArgsPropsAndInputStream() {
		Map<ExtractorOption, String> args = Collections.emptyMap();
		Properties properties = new Properties();
		// properties.put(ExtractorOption.LOCAL_DIR.toString(),
		// "prop_local_dir");
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
		Map<ExtractorOption, String> args = Collections.emptyMap();
		Properties properties = new Properties();
		// properties.put(ExtractorOption.LOCAL_DIR.toString(),
		// "prop_local_dir");
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

	// private InputStream inputStreamFrom(Map<ExtractorOption, String> vals) {
	// String separator = System.getProperty("line.separator");
	// StringBuilder sb = new StringBuilder();
	// // for (String s : strings) {
	// // sb.append(s.replaceAll(separator, " ").trim()).append(separator);
	// // }
	// return new ByteArrayInputStream(sb.toString().getBytes());
	// }

}
