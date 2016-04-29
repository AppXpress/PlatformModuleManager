package com.gtnexus.appxpress.pmdocgen.adapter;

import java.math.BigInteger;
import java.util.Map;

import com.google.common.base.Function;
import com.gtnexus.appxpress.platform.module.model.design.CustomObjectDesignV110;

public abstract class CustomObjectDesignV110DisplayAdapter extends DisplayAdapter<CustomObjectDesignV110> {
	
	// -- Identification Fn's
	protected static final Function<CustomObjectDesignV110, String> NAME_FN = new Function<CustomObjectDesignV110, String>() {
		@Override
		public String apply(CustomObjectDesignV110 design) {
			return design.getName();
		}
	};

	protected static final Function<CustomObjectDesignV110, String> GLOBAL_TYPE_FN = new Function<CustomObjectDesignV110, String>() {
		@Override
		public String apply(CustomObjectDesignV110 design) {
			return design.getGlobalObjectType();
		}
	};

	protected static final Function<CustomObjectDesignV110, String> DESIGN_TYPE_FN = new Function<CustomObjectDesignV110, String>() {
		@Override
		public String apply(CustomObjectDesignV110 design) {
			return design.getDesignType();
		}
	};

	protected static final Function<CustomObjectDesignV110, String> DESCRIPTION_FN = new Function<CustomObjectDesignV110, String>() {
		@Override
		public String apply(CustomObjectDesignV110 design) {
			return design.getDescription();
		}
	};

	protected static final Function<CustomObjectDesignV110, String> NATURAL_IDENTIFIER_FN = new Function<CustomObjectDesignV110, String>() {
		@Override
		public String apply(CustomObjectDesignV110 design) {
			return design.getUniqueIdentifier();
		}
	};

	// TODO
	protected static final Function<CustomObjectDesignV110, String> LICENSEE_FIELD_FN = new Function<CustomObjectDesignV110, String>() {
		@Override
		public String apply(CustomObjectDesignV110 design) {
			// TODO Auto-generated method stub
			return null;
		}
	};

	protected static final Function<CustomObjectDesignV110, String> API_VERSION_FN = new Function<CustomObjectDesignV110, String>() {
		@Override
		public String apply(CustomObjectDesignV110 design) {
			BigInteger version = design.getApiVersion();
			return version == null ? "" : version.toString();
		}
	};

	// -- Runtime Settings Fn's
	// ---------------------------------------------------------------------------------------

	protected static final Function<CustomObjectDesignV110, String> VISIBILITY_MODE_FN = new Function<CustomObjectDesignV110, String>() {
		@Override
		public String apply(CustomObjectDesignV110 design) {
			return design.getVisibilityType();
		}
	};

	protected static final Function<CustomObjectDesignV110, String> DISPLAY_COLUMNS_FN = new Function<CustomObjectDesignV110, String>() {
		@Override
		public String apply(CustomObjectDesignV110 design) {
			return null;
		}
	};

	protected static final Function<CustomObjectDesignV110, String> ANCHOR_TO_FN = new Function<CustomObjectDesignV110, String>() {
		@Override
		public String apply(CustomObjectDesignV110 design) {
			return null;
		}
	};

	protected static final Function<CustomObjectDesignV110, String> ENABLE_DYNAMIC_FIELD_MASKING_FN = new Function<CustomObjectDesignV110, String>() {
		@Override
		public String apply(CustomObjectDesignV110 design) {
			return null;
		}
	};

	protected static final Function<CustomObjectDesignV110, String> ANCHOR_CARDINALITY_FN = new Function<CustomObjectDesignV110, String>() {
		@Override
		public String apply(CustomObjectDesignV110 design) {
			return null;
		}
	};

	protected static final Function<CustomObjectDesignV110, String> FIELD_MASKING_SCRIPT_NAME_FN = new Function<CustomObjectDesignV110, String>() {
		@Override
		public String apply(CustomObjectDesignV110 design) {
			return null;
		}
	};

	protected static final Function<CustomObjectDesignV110, String> REQUIRED_DOC_STATUS_FN = new Function<CustomObjectDesignV110, String>() {
		@Override
		public String apply(CustomObjectDesignV110 design) {
			return null;
		}
	};

	protected static final Function<CustomObjectDesignV110, String> ANCHOR_FILTER_FN = new Function<CustomObjectDesignV110, String>() {
		@Override
		public String apply(CustomObjectDesignV110 design) {
			return null;
		}
	};

	protected static final Function<CustomObjectDesignV110, String> ENABLE_TABLE_MODE_FN = new Function<CustomObjectDesignV110, String>() {
		@Override
		public String apply(CustomObjectDesignV110 design) {
			return null;
		}
	};
	
	/**
	 * 
	 * @param adapterMap
	 */
	protected CustomObjectDesignV110DisplayAdapter(Map<String, Function<CustomObjectDesignV110, String>> adapterMap) {
		super(adapterMap);
		// TODO Auto-generated constructor stub
	}
	


}
