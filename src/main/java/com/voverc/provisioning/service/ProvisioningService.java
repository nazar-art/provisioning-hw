package com.voverc.provisioning.service;

import com.voverc.provisioning.entity.ConfigurationFileResponse;

public interface ProvisioningService {

    ConfigurationFileResponse getProvisioningFile(String macAddress);

}
