package com.gtnexus.appxpress.platform.module;

import java.util.LinkedList;
import java.util.List;

import com.gtnexus.appxpress.platform.module.model.customaction.CustomActionD1;
import com.gtnexus.appxpress.platform.module.model.design.CustomObjectDesignV110;
import com.gtnexus.appxpress.platform.module.model.platformmodule.PlatformModuleXml;
import com.gtnexus.appxpress.platform.module.model.typeextension.TypeExtensionD1;

public class ModuleVO {
	
	private final PlatformModuleXml platformModuleXml;
	private final List<CustomActionD1> customActions;
	private final List<TypeExtensionD1> typeExtensions;
	private final List<CustomObjectDesignV110> designs;
	
	public ModuleVO(PlatformModuleXml platformModuleXml, List<CustomActionD1> customActions, List<TypeExtensionD1> typeExtensions,
			List<CustomObjectDesignV110> designs) {
		super();
		this.platformModuleXml = platformModuleXml;
		this.customActions = customActions;
		this.typeExtensions = typeExtensions;
		this.designs = designs;
	}

	public PlatformModuleXml getPlatformModuleXml() {
		return platformModuleXml;
	}

	public List<CustomActionD1> getCustomActions() {
		return customActions;
	}
	
	public List<TypeExtensionD1> getTypeExtensions() {
		return typeExtensions;
	}

	public List<CustomObjectDesignV110> getDesigns() {
		return designs;
	}
	
	public static class Builder {
		private  PlatformModuleXml platformModuleXml;
		private  List<CustomActionD1> customActions;
		private  List<TypeExtensionD1> typeExtensions;
		private  List<CustomObjectDesignV110> designs;
		
		public Builder() {
			this.customActions = new LinkedList<>();
			this.typeExtensions = new LinkedList<>();
			this.designs = new LinkedList<>();
		}
		
		public Builder addCustomAction(CustomActionD1 ca) {
			this.customActions.add(ca);
			return this;
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
			return new ModuleVO(platformModuleXml, customActions, typeExtensions, designs);
		}
	}
}
