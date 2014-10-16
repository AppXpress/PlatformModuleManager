package com.gtnexus.appxpress.pmextractor;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.EnumSet;
import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.assertTrue;

/**
 * Created by jjdonov on 9/29/14.
 */
public class ArgsAndPropertiesConsolidatorTest {

    @Test
    public void testWithArgsAndNullProps() {
        //TODO this will change with the behavior of args. REVISIT ME
        String[] args = {"arg_prop_name", "arg_local_dir",
                         "arg_customer", "arg_platform",
                         "arg_overwriteScripts", "arg_overwriteFEF"};
        Properties properties = new Properties();
        ArgsAndPropertiesConsolidator consolidator = new ArgsAndPropertiesConsolidator(args, properties,
                inputStreamFrom(args), System.out);
        Map<ExtractorOption, String> consolidated = consolidator.consolidate();
        for(ExtractorOption option : EnumSet.allOf(ExtractorOption.class)) {
            assertTrue(consolidated.get(option).startsWith("arg_"));
        }
    }

    @Test
    public void testWithArgsAndProps() {
        String[] args = {"arg_prop_name", null, //"arg_local_dir",
                "arg_customer", null, //"arg_platform",
                "arg_overwriteScripts", "arg_overwriteFEF"};
        Properties properties = new Properties();
        properties.put(ExtractorOption.LOCAL_DIR.toString(), "prop_local_dir");
        properties.put(ExtractorOption.PLATFORM.toString(), "prop_platform");
        ArgsAndPropertiesConsolidator consolidator = new ArgsAndPropertiesConsolidator(args, properties,
                inputStreamFrom(), System.out);
        Map<ExtractorOption, String> consolidated = consolidator.consolidate();
        for(ExtractorOption option : EnumSet.allOf(ExtractorOption.class)) {
            if(option.equals(ExtractorOption.LOCAL_DIR) ||
                    option.equals(ExtractorOption.PLATFORM)) {
                assertTrue(consolidated.get(option).startsWith("prop_"));
            } else {
                assertTrue(consolidated.get(option) + " does not start with arg_.",
                        consolidated.get(option).startsWith("arg_"));
            }
        }
    }

    @Test
    public void testWithArgsPropsAndInputStream() {
        String[] args = {"arg_prop_name", null, //"arg_local_dir",
                "arg_customer", null, //"arg_platform",
                "arg_overwriteScripts", null}; //arg_overwriteFEF:boolean
        Properties properties = new Properties();
        //properties.put(ExtractorOption.LOCAL_DIR.toString(), "prop_local_dir");
        properties.put(ExtractorOption.PLATFORM.toString(), "prop_platform");
        ArgsAndPropertiesConsolidator consolidator = new ArgsAndPropertiesConsolidator(args, properties,
                inputStreamFrom("some_local_dir", "Y"), System.out);
        Map<ExtractorOption, String> consolidated = consolidator.consolidate();
        assertTrue(consolidated.get(ExtractorOption.LOCAL_DIR).equals("some_local_dir"));
        assertTrue(consolidated.get(ExtractorOption.OVERWRITE_FEF).equals("Y"));
    }

    private InputStream inputStreamFrom(String... strings) {
        String separator = System.getProperty("line.separator");
        StringBuilder sb = new StringBuilder();
        for(String s : strings) {
            sb.append(s.replaceAll(separator, " ").trim())
                    .append(separator);
        }
        return new ByteArrayInputStream(sb.toString().getBytes());
    }



}
