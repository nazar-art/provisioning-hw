package com.voverc.provisioning.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;


@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
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