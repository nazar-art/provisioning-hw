package com.voverc.provisioning.dataprovider.impl;

import com.voverc.provisioning.dataprovider.ConfigurationDataProvider;
import com.voverc.provisioning.model.ProvisioningData;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Nazar Lelyak.
 */
public class ConferenceConfigurationDP implements ConfigurationDataProvider {

    private JsonParser jsonParser = JsonParserFactory.getJsonParser();

    @Override
    public Map<String, String> parseOverrideFragments(String fragment) {
        return jsonParser.parseMap(fragment)
                .entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> String.valueOf(entry.getValue())));
    }

    @Override
    public String formatProvisioningData(ProvisioningData data) {
        return data.toJson();
    }
}
