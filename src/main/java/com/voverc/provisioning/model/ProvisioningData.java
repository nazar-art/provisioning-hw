package com.voverc.provisioning.model;

import com.google.common.collect.Maps;
import com.google.gson.Gson;

import java.util.Map;

/**
 * @author Nazar Lelyak.
 */
public class ProvisioningData {

    private final Gson gson = new Gson();
    private final Map<String, String> data = Maps.newLinkedHashMap();

    public Map<String, String> put(String key, String value) {
        data.put(key, value);
        return data;
    }

    public void put(CommonConfigurationKeys key, String value) {
        data.put(key.getLowerName(), value);
    }

    public void putAll(Map<String, String> map) {
        map.forEach(this::put);
    }

    public String get(String key) {
        return data.get(key);
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
