package com.gtnexus.pmm.docgen.renderer.excel;

public interface SheetRenderer<X> {
    public void render(X source);

    public int getMaxWidth();
}
