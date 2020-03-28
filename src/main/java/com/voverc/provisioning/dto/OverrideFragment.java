package com.voverc.provisioning.dto;

/**
 * @author Nazar Lelyak.
 */
public enum FragmentNames {
    DOMAIN, PORT, TIMEOUT;

    public String getLowerCaseName() {
        return this.toString().toLowerCase();
    }
}
