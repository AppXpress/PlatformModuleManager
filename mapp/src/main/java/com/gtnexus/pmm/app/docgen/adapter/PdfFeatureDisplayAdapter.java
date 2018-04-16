package com.gtnexus.pmm.app.docgen.adapter;

import java.util.Map;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.gtnexus.pmm.app.platform.module.model.design.PdfFeature;

public class PdfFeatureDisplayAdapter extends DisplayAdapter<PdfFeature> {
    private static final Function<PdfFeature, String> IS_ENABLED_FN = new BooleanDisplayFunction<PdfFeature>() {
	@Override
	public String applyToNonNull(PdfFeature pdf) {
	    return Boolean.toString(pdf.isEnabled());
	}
    };

    private static final Map<String, Function<PdfFeature, String>> adapterMap = new ImmutableMap.Builder<String, Function<PdfFeature, String>>()
	    .put("Enabled", IS_ENABLED_FN).build();

    public PdfFeatureDisplayAdapter() {
	super(adapterMap);
    }

}
