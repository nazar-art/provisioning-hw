package com.voverc.provisioning.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Nazar Lelyak.
 */
@Data
@NoArgsConstructor
public class FragmentDTO {
    private String domain;
    private int port;
    private int timeout;
}
