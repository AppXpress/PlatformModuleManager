package com.gtnexus.appxpress.pmdocgen.adapter;

import java.util.Map;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.gtnexus.appxpress.platform.module.model.template.TemplateMediaV110;

public class TemplateDisplayAdapter extends DisplayAdapter<TemplateMediaV110> {

    private static final Function<TemplateMediaV110, String> DESCRIPTION_FN = new BooleanDisplayFunction<TemplateMediaV110>() {
	@Override
	public String applyToNonNull(TemplateMediaV110 tm) {
	    return tm.getDescription();
	}
    };

    private static final Function<TemplateMediaV110, String> TEMPLATE_NAME_FN = new PrimitiveDisplayFunction<TemplateMediaV110>() {
	@Override
	public String applyToNonNull(TemplateMediaV110 tm) {
	    return tm.getTemplateName();
	}
    };

    private static final Function<TemplateMediaV110, String> DATA_MARKUP_TYPE_FN = new PrimitiveDisplayFunction<TemplateMediaV110>() {
	@Override
	public String applyToNonNull(TemplateMediaV110 tm) {
	    return tm.getDataMarkupType();
	}
    };

    private static final Function<TemplateMediaV110, String> RENDER_MARKUP_TYPE_FN = new PrimitiveDisplayFunction<TemplateMediaV110>() {
	@Override
	public String applyToNonNull(TemplateMediaV110 tm) {
	    return tm.getRenderMarkupType();
	}
    };

    private static final Function<TemplateMediaV110, String> MEMO_FN = new PrimitiveDisplayFunction<TemplateMediaV110>() {
	@Override
	public String applyToNonNull(TemplateMediaV110 tm) {
	    return tm.getMemo();
	}
    };

    private static final Map<String, Function<TemplateMediaV110, String>> adapterMap = new ImmutableMap.Builder<String, Function<TemplateMediaV110, String>>()
	    .put("Template Name", TEMPLATE_NAME_FN).put("File", DESCRIPTION_FN)
	    .put("Data Markup Type", DATA_MARKUP_TYPE_FN).put("Render Markup Type", RENDER_MARKUP_TYPE_FN)
	    .put("Memo", MEMO_FN).build();

    public TemplateDisplayAdapter() {
	super(adapterMap);
    }

}
