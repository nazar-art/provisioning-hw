package com.voverc.provisioning.dto;

/**
 * @author Nazar Lelyak.
 */
public enum FragmentName {
    DOMAIN, PORT, TIMEOUT;

    public String getLowerCaseName() {
        return this.toString().toLowerCase();
    }
}
