package com.gtnexus.pmm.platform.module.interpretation;

import java.util.Comparator;

import com.google.common.collect.Ordering;
import com.gtnexus.pmm.platform.module.model.design.CustomObjectDesignV110;

public class CustomObjectDesignV110Comparator implements Comparator<CustomObjectDesignV110> {

    Ordering<String> naturalOrdering = Ordering.natural();

    @Override
    public int compare(CustomObjectDesignV110 d1, CustomObjectDesignV110 d2) {
	if (d1.getDesignType() == null && d2.getDesignType() == null) {
	    return naturalOrderingOfName(d1, d2);
	}
	if (d1.getDesignType() == null && d2.getDesignType() != null) {
	    return 1;
	}
	if (d1.getDesignType() != null && d2.getDesignType() == null) {
	    return -1;
	}
	if ((isPrimary(d1) && isPrimary(d2)) || !isPrimary(d1) && !isPrimary(d2)) {
	    return naturalOrderingOfName(d1, d2);
	} else if (!isPrimary(d1) && isPrimary(d2)) {
	    return 1;
	} else { // (isPrimary(d1) && !isPrimary(d2))
	    return -1;
	}
    }

    private int naturalOrderingOfName(CustomObjectDesignV110 d1, CustomObjectDesignV110 d2) {
	return naturalOrdering.compare(d1.getName(), d2.getName());
    }

    private boolean isPrimary(CustomObjectDesignV110 d) {
	return d.getDesignType().equalsIgnoreCase("primary");
    }
}