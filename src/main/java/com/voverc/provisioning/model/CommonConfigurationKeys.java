package com.voverc.provisioning.model;

/**
 * @author Nazar Lelyak.
 */
public enum CommonConfigurationKeys {
    USERNAME,
    PASSWORD,
    DOMAIN,
    PORT,
    CODECS;

    public String getLowerName() {
        return this.toString().toLowerCase();
    }
}
