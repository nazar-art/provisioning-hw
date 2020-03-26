package com.voverc.provisioning.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Device {

    @Id
    @Column(name = "mac_address")
    private String macAddress;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DeviceModel model;

    @Column(name = "override_fragment")
    private String overrideFragment;

    private String username;

    private String password;

    public enum DeviceModel {
        CONFERENCE,
        DESK
    }
}