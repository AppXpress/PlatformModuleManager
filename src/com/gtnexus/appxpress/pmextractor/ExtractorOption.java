package com.gtnexus.appxpress.pmextractor;

public enum ExtractorOption {
	PLATFORM_ZIP("platformZip", String.class), LOCAL_DIR("localDir",
			String.class), CUSTOMER("customer", String.class), PLATFORM(
			"platform", String.class), OVERWRITE_SCRIPTS(
			"overwriteScripts", Boolean.class), OVERWRITE_FEF(
			"overwriteFed", Boolean.class);

	private final String name;
	private final Class<?> type;

	private ExtractorOption(String name, Class<?> type) {
		this.name = name;
		this.type = type;
	}

	@Override
	public String toString() {
		return name;
	}

	public Class<?> getType() {
		return type;
	}
}
