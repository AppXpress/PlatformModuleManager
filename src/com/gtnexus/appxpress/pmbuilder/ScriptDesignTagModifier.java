package com.gtnexus.appxpress.pmbuilder;


public interface ScriptDesignTagModifier {
	
	public static final String XML_DESIGN_TAG = "CustomObjectDesignV110";
	public static final String MIME_TYPE = "mimeType";
	public static final String FIELD_MASKING_ENABLED = "fieldMaskingEnabled";
	public static final String FILENAME = "fileName";
	public static final String DESCRIPTION = "description";
	
	public void modifyAsNeeded();
	
}
