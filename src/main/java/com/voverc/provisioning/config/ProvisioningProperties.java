package com.voverc.provisioning.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author Nazar Lelyak.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties("provisioning")
public class ProvisioningProperties {

    private String domain;
    private int port;
    @Singular
    private List<String> codecs;
}
