package com.voverc.provisioning.dataprovider;

import com.google.common.collect.Maps;
import com.voverc.provisioning.model.ProvisioningData;
import lombok.Cleanup;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.Map;
import java.util.Properties;

/**
 * @author Nazar Lelyak.
 */
public class DeskConfigurationDP implements ConfigurationDataProvider {

    private final Properties properties = new Properties();

    @Override
    @SneakyThrows
    public Map<String, String> parseOverrideFragments(String fragment) {
        @Cleanup
        BufferedReader reader = new BufferedReader(new StringReader(fragment));

        properties.load(reader);

        return Maps.fromProperties(properties);
    }

    @Override
    public String formatProvisioningData(ProvisioningData data) {
        return data.toProperties();
    }
}
