package com.gtnexus.appxpress.platform.module;

import java.util.LinkedList;
import java.util.List;

import com.gtnexus.appxpress.platform.module.model.design.CustomObjectDesignV110;
import com.gtnexus.appxpress.platform.module.model.platformmodule.PlatformModuleXml;
import com.gtnexus.appxpress.platform.module.model.typeextension.TypeExtensionD1;

public class ModuleVO {
	
	private final PlatformModuleXml platformModuleXml;
	private final List<TypeExtensionD1> typeExtensions;
	private final List<CustomObjectDesignV110> designs;
	
	public ModuleVO(PlatformModuleXml platformModuleXml, List<TypeExtensionD1> typeExtensions,
			List<CustomObjectDesignV110> designs) {
		super();
		this.platformModuleXml = platformModuleXml;
		this.typeExtensions = typeExtensions;
		this.designs = designs;
	}

	public PlatformModuleXml getPlatformModuleXml() {
		return platformModuleXml;
	}

	public List<TypeExtensionD1> getTypeExtensions() {
		return typeExtensions;
	}

	public List<CustomObjectDesignV110> getDesigns() {
		return designs;
	}
	
	public static class Builder {
		private  PlatformModuleXml platformModuleXml;
		private  List<TypeExtensionD1> typeExtensions;
		private  List<CustomObjectDesignV110> designs;
		
		public Builder() {
			this.typeExtensions = new LinkedList<>();
			this.designs = new LinkedList<>();
		}
		
		public Builder addTypeExtension(TypeExtensionD1 te) {
			this.typeExtensions.add(te);
			return this;
		}
		
		public Builder addDesign(CustomObjectDesignV110 design) {
			this.designs.add(design);
			return this;
		}
		
		public Builder setPlatformModuleXml(PlatformModuleXml platMod) {
			this.platformModuleXml = platMod;
			return this;
		}
		
		public ModuleVO build() {
			return new ModuleVO(platformModuleXml, typeExtensions, designs);
		}
	}
}
