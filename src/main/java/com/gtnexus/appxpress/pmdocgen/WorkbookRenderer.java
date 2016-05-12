package com.gtnexus.appxpress.pmdocgen;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.gtnexus.appxpress.platform.module.ModuleVO;
import com.gtnexus.appxpress.platform.module.interpretation.CustomObjectDesignV110Comparator;
import com.gtnexus.appxpress.platform.module.model.design.CustomObjectDesignV110;
import com.gtnexus.appxpress.pmdocgen.renderer.excel.ChangeLogRenderer;
import com.gtnexus.appxpress.pmdocgen.renderer.excel.CustomObjectDesignDocRenderer;
import com.gtnexus.appxpress.pmdocgen.renderer.excel.TypeExtensionDocRenderer;

public class WorkbookRenderer {

	private static final int MAX_SHEET_NAME_LENGTH = 31;
	private boolean useFullNames = true;
	private Map<String, String> nameWithoutPrefix;
	
	public WorkbookRenderer() {
	}

	public XSSFWorkbook render(ModuleVO vo) {
		XSSFWorkbook wb = new XSSFWorkbook();
		new ChangeLogRenderer(wb).render(vo.getPlatformModuleXml());
		new TypeExtensionDocRenderer(wb).render(vo.getTypeExtensions());
		List<CustomObjectDesignV110> designs = vo.getDesigns();
		Collections.sort(designs, new CustomObjectDesignV110Comparator());
		preProcessDesignNames(designs);
		for(CustomObjectDesignV110 design : designs) {
			if(useFullNames) {
				new CustomObjectDesignDocRenderer(wb).render(design);
			} else {
				new CustomObjectDesignDocRenderer(wb, nameWithoutPrefix).render(design);
			}
			
		}
		return wb;
	}
	
	private void preProcessDesignNames(List<CustomObjectDesignV110> designs) {
		if(designs.size() == 1) {
			return;
		}
		List<String> longNames = new LinkedList<>();
		for (CustomObjectDesignV110 design : designs) {
			String name = design.getName();
			if(name.length() >= MAX_SHEET_NAME_LENGTH) {
				longNames.add(name);
			}
		}
		if(longNames.size() < 2) {
			return;
		}
		String lcp = longestCommonPrefix(longNames);
		if(lcp.equals("")) { //TODO: this is too naive, we should have a threshold, or check that the prefix makes sense.
			useFullNames = true;
		} else {
			useFullNames = false;
			buildNameWithoutPrefixMap(designs, lcp);
		}
	}
	
	private static String longestCommonPrefix(List<String> strings) {
	    if (strings.isEmpty()){
	    	return "";
	    }
	    String prefix = strings.get(0);
	    for (String s : strings)
	        while (s.indexOf(prefix) != 0) {
	            prefix = prefix.substring(0, prefix.length() - 1);
	            if (prefix.isEmpty()){
	            	return "";
	            }
	        }        
	    return prefix;
	}
	
	private void buildNameWithoutPrefixMap(List<CustomObjectDesignV110> designs, String lcp) {
		Builder<String, String> builder = new ImmutableMap.Builder<String, String>();
		for(CustomObjectDesignV110 design : designs) {
			builder.put(design.getName(), design.getName().substring(lcp.length()));
		}
		nameWithoutPrefix = builder.build();
	}
	
}
