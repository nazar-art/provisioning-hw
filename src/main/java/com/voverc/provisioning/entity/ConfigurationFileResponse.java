package com.voverc.provisioning.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Nazar Lelyak.
 */
@Data
@NoArgsConstructor
public class ConfigurationFileResponse {
    private String username;
    private String password;
    private String domain;
    private int port;
    private List<String> codecs;
    private int timeout;
}
