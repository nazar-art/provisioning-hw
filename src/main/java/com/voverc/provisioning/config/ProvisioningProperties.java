package com.voverc.provisioning.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author Nazar Lelyak.
 */
@Data
@ConfigurationProperties("provisioning")
public class ProvisioningProperties {
    private String domain;
    private int port;
    private List<String> codecs;
}
