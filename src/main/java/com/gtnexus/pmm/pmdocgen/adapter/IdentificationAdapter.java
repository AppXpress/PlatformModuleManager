package com.gtnexus.pmm.pmdocgen.adapter;

import java.util.Map;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.gtnexus.pmm.platform.module.model.design.CustomObjectDesignV110;

public class IdentificationAdapter extends CustomObjectDesignV110DisplayAdapter {

    private static final Map<String, Function<CustomObjectDesignV110, String>> adapterMap = new ImmutableMap.Builder<String, Function<CustomObjectDesignV110, String>>()
	    .put("Design Name", CustomObjectDesignV110DisplayAdapter.NAME_FN)
	    .put("Global Type", CustomObjectDesignV110DisplayAdapter.GLOBAL_TYPE_FN)
	    .put("Design Type", CustomObjectDesignV110DisplayAdapter.DESIGN_TYPE_FN)
	    .put("Description", CustomObjectDesignV110DisplayAdapter.DESCRIPTION_FN)
	    .put("Natural Identifier", CustomObjectDesignV110DisplayAdapter.NATURAL_IDENTIFIER_FN)
	    .put("Licensee Field", CustomObjectDesignV110DisplayAdapter.LICENSEE_FIELD_FN)
	    .put("API Version", CustomObjectDesignV110DisplayAdapter.API_VERSION_FN).build();

    public IdentificationAdapter() {
	super(adapterMap);
    }

}
