package com.voverc.provisioning.dataprovider;

import com.voverc.provisioning.config.ProvisioningProperties;
import com.voverc.provisioning.entity.Device;
import com.voverc.provisioning.model.ProvisioningData;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

import static com.voverc.provisioning.model.CommonConfigurationKeys.CODECS;
import static com.voverc.provisioning.model.CommonConfigurationKeys.DOMAIN;
import static com.voverc.provisioning.model.CommonConfigurationKeys.PASSWORD;
import static com.voverc.provisioning.model.CommonConfigurationKeys.PORT;
import static com.voverc.provisioning.model.CommonConfigurationKeys.USERNAME;


/**
 * @author Nazar Lelyak.
 */
public abstract class BaseConfigurationDataProvider {

    public ProvisioningData getConfiguration(Device device, ProvisioningProperties properties) {
        ProvisioningData data = new ProvisioningData();

        data.put(USERNAME, device.getUsername());
        data.put(PASSWORD, device.getPassword());
        data.put(DOMAIN, properties.getDomain());
        data.put(PORT, String.valueOf(properties.getPort()));
        data.put(CODECS, String.valueOf(properties.getCodecs()));

        if (StringUtils.isNotEmpty(device.getOverrideFragment())) {
            data.putAll(parseOverrideFragments(device.getOverrideFragment()));
        }

        return data;
    }

    protected abstract Map<String, String> parseOverrideFragments(String fragment);

    public abstract String formatProvisioningData(ProvisioningData provisioningData);
}
