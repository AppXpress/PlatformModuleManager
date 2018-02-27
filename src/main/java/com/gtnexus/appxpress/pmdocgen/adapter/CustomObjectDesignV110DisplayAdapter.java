package com.gtnexus.appxpress.pmdocgen.adapter;

import java.util.Map;

import com.google.common.base.Function;
import com.gtnexus.appxpress.platform.module.model.design.Anchor;
import com.gtnexus.appxpress.platform.module.model.design.CodSection;
import com.gtnexus.appxpress.platform.module.model.design.CustomObjectDesignV110;

public abstract class CustomObjectDesignV110DisplayAdapter extends DisplayAdapter<CustomObjectDesignV110> {

    // -- Identification Fn's
    protected static final Function<CustomObjectDesignV110, String> NAME_FN = new PrimitiveDisplayFunction<CustomObjectDesignV110>() {
	@Override
	public String applyToNonNull(CustomObjectDesignV110 design) {
	    return design.getName();
	}
    };

    protected static final Function<CustomObjectDesignV110, String> GLOBAL_TYPE_FN = new PrimitiveDisplayFunction<CustomObjectDesignV110>() {
	@Override
	public String applyToNonNull(CustomObjectDesignV110 design) {
	    return design.getGlobalObjectType();
	}
    };

    protected static final Function<CustomObjectDesignV110, String> DESIGN_TYPE_FN = new PrimitiveDisplayFunction<CustomObjectDesignV110>() {
	@Override
	public String applyToNonNull(CustomObjectDesignV110 design) {
	    return design.getDesignType();
	}
    };

    protected static final Function<CustomObjectDesignV110, String> DESCRIPTION_FN = new PrimitiveDisplayFunction<CustomObjectDesignV110>() {
	@Override
	public String applyToNonNull(CustomObjectDesignV110 design) {
	    return design.getDescription();
	}
    };

    protected static final Function<CustomObjectDesignV110, String> NATURAL_IDENTIFIER_FN = new PrimitiveDisplayFunction<CustomObjectDesignV110>() {
	@Override
	public String applyToNonNull(CustomObjectDesignV110 design) {
	    return design.getUniqueIdentifier();
	}
    };

    protected static final Function<CustomObjectDesignV110, String> LICENSEE_FIELD_FN = new PrimitiveDisplayFunction<CustomObjectDesignV110>() {
	@Override
	public String applyToNonNull(CustomObjectDesignV110 design) {
	    return design.getLicenseeField();
	}
    };

    protected static final Function<CustomObjectDesignV110, String> API_VERSION_FN = new PrimitiveDisplayFunction<CustomObjectDesignV110>() {
	@Override
	public String applyToNonNull(CustomObjectDesignV110 design) {
	    return design.getApiVersion() == null ? "" : design.getApiVersion().toString();
	}
    };

    // -- Runtime Settings Fn's
    // ---------------------------------------------------------------------------------------

    protected static final Function<CustomObjectDesignV110, String> VISIBILITY_MODE_FN = new PrimitiveDisplayFunction<CustomObjectDesignV110>() {
	@Override
	public String applyToNonNull(CustomObjectDesignV110 design) {
	    return design.getVisibilityType();
	}
    };

    protected static final Function<CustomObjectDesignV110, String> DISPLAY_COLUMNS_FN = new PrimitiveDisplayFunction<CustomObjectDesignV110>() {
	@Override
	public String applyToNonNull(CustomObjectDesignV110 design) {
	    CodSection codSection = design.getCodSection();
	    if (codSection != null) {
		return codSection.getNumberOfDisplayColumns() == null ? ""
			: codSection.getNumberOfDisplayColumns().toString();
	    }
	    return null;
	}
    };

    protected static final Function<CustomObjectDesignV110, String> ANCHOR_TO_FN = new PrimitiveDisplayFunction<CustomObjectDesignV110>() {
	@Override
	public String applyToNonNull(CustomObjectDesignV110 design) {
	    return design.getAnchor() != null ? design.getAnchor().getTransactionType() : null;
	}
    };

    protected static final Function<CustomObjectDesignV110, String> ENABLE_DYNAMIC_FIELD_MASKING_FN = new PrimitiveDisplayFunction<CustomObjectDesignV110>() {
	@Override
	public String applyToNonNull(CustomObjectDesignV110 design) {
	    return null;
	}
    };

    protected static final Function<CustomObjectDesignV110, String> ANCHOR_CARDINALITY_FN = new PrimitiveDisplayFunction<CustomObjectDesignV110>() {
	final String LIST = "List";
	final String SINGLE = "Single";

	@Override
	public String applyToNonNull(CustomObjectDesignV110 design) {
	    Anchor anchor = design.getAnchor();
	    if (anchor != null) {
		return design.getAnchor().isIsCollection() ? LIST : SINGLE;
	    }
	    return null;
	}
    };

    // TODO
    protected static final Function<CustomObjectDesignV110, String> FIELD_MASKING_SCRIPT_NAME_FN = new PrimitiveDisplayFunction<CustomObjectDesignV110>() {
	@Override
	public String applyToNonNull(CustomObjectDesignV110 design) {
	    return null;
	}
    };

    // TODO
    protected static final Function<CustomObjectDesignV110, String> REQUIRED_DOC_STATUS_FN = new PrimitiveDisplayFunction<CustomObjectDesignV110>() {
	@Override
	public String applyToNonNull(CustomObjectDesignV110 design) {
	    return null;
	}
    };

    // TODO
    protected static final Function<CustomObjectDesignV110, String> ANCHOR_FILTER_FN = new PrimitiveDisplayFunction<CustomObjectDesignV110>() {
	@Override
	public String applyToNonNull(CustomObjectDesignV110 design) {
	    return null;
	}
    };

    // TODO
    protected static final Function<CustomObjectDesignV110, String> ENABLE_TABLE_MODE_FN = new PrimitiveDisplayFunction<CustomObjectDesignV110>() {
	@Override
	public String applyToNonNull(CustomObjectDesignV110 design) {
	    return Boolean.toString(design.isUseTableSupport());
	}
    };

    /**
     * 
     * @param adapterMap
     */
    protected CustomObjectDesignV110DisplayAdapter(Map<String, Function<CustomObjectDesignV110, String>> adapterMap) {
	super(adapterMap);
    }
}
