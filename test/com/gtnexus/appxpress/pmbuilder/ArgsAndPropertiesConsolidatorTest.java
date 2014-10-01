package com.gtnexus.appxpress.pmbuilder;

import com.gtnexus.appxpress.pmextractor.ArgsAndPropertiesConsolidator;
import com.gtnexus.appxpress.pmextractor.ExtractorOption;
import org.junit.Test;

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
        String[] args = {"arg_prop_name", "arg_local_dir",
                         "arg_customer", "arg_platform"};

        ArgsAndPropertiesConsolidator consolidator = new ArgsAndPropertiesConsolidator(args, properties);
        Map<ExtractorOption, String> consolidated = consolidator.consolidate();
        for(ExtractorOption option : EnumSet.allOf(ExtractorOption.class)) {

        }
        /*properties.setProperty(ExtractorOption.CUSTOMER.toString(),
                "Properties_Customer");
        properties.setProperty()*/


    }




}
