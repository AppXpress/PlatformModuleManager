package com.gtnexus.appxpress.pmdocgen.renderer.excel;

public interface SheetRenderer<X> {
	public void render(X source);
	public int getMaxWidth();
}
