package com.voverc.provisioning.service;

import com.voverc.provisioning.model.ConfigurationFileResponse;

public interface ProvisioningService {

    ConfigurationFileResponse getProvisioningFile(String macAddress);

}
