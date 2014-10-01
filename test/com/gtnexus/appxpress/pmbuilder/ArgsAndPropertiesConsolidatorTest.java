package com.gtnexus.appxpress.pmbuilder;

import com.gtnexus.appxpress.pmextractor.ArgsAndPropertiesConsolidator;
import com.gtnexus.appxpress.pmextractor.ExtractorOption;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.EnumSet;
import java.util.Map;
import java.util.Properties;

/**
 * Created by jjdonov on 9/29/14.
 */
public class ArgsAndPropertiesConsolidatorTest {

    @Test
    public void testWithArgsAndNullProps() {
        Properties properties = new Properties();
        //TODO this will change with the behavior of args. REVISIT ME
        String[] args = {"arg_prop_name", "arg_local_dir",
                         "arg_customer", "arg_platform",
                         "arg_overwriteScripts", "args_overwriteFEF"};
        ArgsAndPropertiesConsolidator consolidator = new ArgsAndPropertiesConsolidator(args, properties,
                inputStreamFrom(args), System.out);
        Map<ExtractorOption, String> consolidated = consolidator.consolidate();
        for(ExtractorOption option : EnumSet.allOf(ExtractorOption.class)) {
            assertEquals(true, consolidated.get(option).startsWith("arg_"));
        }
        /*properties.setProperty(ExtractorOption.CUSTOMER.toString(),
                "Properties_Customer");
        properties.setProperty()*/
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
