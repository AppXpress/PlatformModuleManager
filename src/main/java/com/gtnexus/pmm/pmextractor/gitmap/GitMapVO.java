package com.gtnexus.pmm.pmextractor.gitmap;

import static com.gtnexus.pmm.AppXpressConstants.PLATFORM_MODULE_UNZIP_NAME;

import java.io.File;
import java.util.Map;

import com.gtnexus.pmm.cli.option.CommandOption;
import com.gtnexus.pmm.pmextractor.cli.ExtractorOption;

public class GitMapVO {

    private final File platformZip;
    private final File localDir;
    private final String customer;
    private final String platform;
    private final File customerDir;
    private final File platformDir;
    private final File unzipDir;
    private final boolean overwriteScripts;
    private final boolean overwriteFef;

    public GitMapVO(Map<CommandOption, String> optionMap) {
	this.localDir = new File(optionMap.get(ExtractorOption.LOCAL_DIR));
	this.customer = optionMap.get(ExtractorOption.CUSTOMER);
	this.platform = optionMap.get(ExtractorOption.MODULE);
	this.customerDir = localDir.toPath().resolve(customer).toFile();
	this.platformDir = customerDir.toPath().resolve(platform).toFile();
	this.platformZip = new File(optionMap.get(ExtractorOption.PLATFORM_ZIP));
	this.unzipDir = new File(PLATFORM_MODULE_UNZIP_NAME);
	this.overwriteScripts = optionMap.get(ExtractorOption.OVERWRITE_SCRIPTS).equalsIgnoreCase("y");
	this.overwriteFef = optionMap.get(ExtractorOption.OVERWRITE_FEF).equalsIgnoreCase("y");
    }

    public File getPlatformZip() {
	return platformZip;
    }

    public File getLocalDir() {
	return localDir;
    }

    public String getCustomer() {
	return customer;
    }

    public String getPlatform() {
	return platform;
    }

    public File getCustomerDir() {
	return customerDir;
    }

    public File getPlatformDir() {
	return platformDir;
    }

    public File getUnzipDir() {
	return unzipDir;
    }

    public boolean isOverwriteScripts() {
	return overwriteScripts;
    }

    public boolean isOverwriteFef() {
	return overwriteFef;
    }

}
