package com.gtnexus.appxpress.pmdocgen.adapter;

import java.util.Map;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.gtnexus.appxpress.platform.module.model.design.CustomObjectDesignV110;

public class RuntimeSettingsAdapter extends CustomObjectDesignV110DisplayAdapter {

	private static final Map<String, Function<CustomObjectDesignV110, String>> adapterMap = 
			new ImmutableMap.Builder<String, Function<CustomObjectDesignV110,String>>()
				.put("Visibility Mode", CustomObjectDesignV110DisplayAdapter.VISIBILITY_MODE_FN)
				.put("Number of Display Columns", CustomObjectDesignV110DisplayAdapter.DISPLAY_COLUMNS_FN)
				.put("Anchor To", CustomObjectDesignV110DisplayAdapter.ANCHOR_TO_FN)
				.put("Enable Dynamic Field Masking", CustomObjectDesignV110DisplayAdapter.ENABLE_DYNAMIC_FIELD_MASKING_FN)
				.put("Anchor Cardinality", CustomObjectDesignV110DisplayAdapter.ANCHOR_CARDINALITY_FN)
				.put("Field Masking Script Function Name", CustomObjectDesignV110DisplayAdapter.FIELD_MASKING_SCRIPT_NAME_FN)
				.put("Required Document Status", CustomObjectDesignV110DisplayAdapter.REQUIRED_DOC_STATUS_FN)
				.put("Anchor Filter", CustomObjectDesignV110DisplayAdapter.ANCHOR_FILTER_FN)
				.put("Enable Table Mode", CustomObjectDesignV110DisplayAdapter.ENABLE_TABLE_MODE_FN)
				.build();
	
	public RuntimeSettingsAdapter() {
		super(adapterMap);
	}

}
