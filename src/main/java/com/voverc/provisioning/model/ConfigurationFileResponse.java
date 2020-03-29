package com.voverc.provisioning.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;

import java.util.List;

/**
 * @author Nazar Lelyak.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfigurationFileResponse {
    private String username;
    private String password;
    private String domain;
    private int port;
    @Singular
    private List<String> codecs;
    private int timeout;
}
