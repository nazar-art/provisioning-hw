package com.voverc.provisioning.model;

import com.google.common.collect.Maps;
import com.google.gson.Gson;

import java.util.Map;

/**
 * @author Nazar Lelyak.
 */
public class ProvisioningData {

    private final Gson gson;
    private final Map<String, String> data;

    public ProvisioningData() {
        gson = new Gson();
        data = Maps.newLinkedHashMap();
    }

    public void put(String key, String value) {
        data.put(key, value);
    }

    public void put(CommonConfigurationKeys key, String value) {
        data.put(key.getLowerName(), value);
    }

    public void putAll(Map<String, String> map) {
//        map.forEach(this::put);
        data.putAll(map);
    }

    public String toJson() {
        return gson.toJson(data);
    }

    public String toProperties() {
        StringBuilder sb = new StringBuilder();
        data.forEach((key, value) -> sb.append(key).append("=").append(value).append(System.lineSeparator()));
        return sb.toString();
    }
}
