package com.voverc.provisioning.controller;

import com.voverc.provisioning.service.ProvisioningServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@AllArgsConstructor
@RequestMapping("/api/v1")
public class ProvisioningController {

    private final ProvisioningServiceImpl provisioningService;

    @GetMapping(value = "/provisioning/{macAddress}")
    public ResponseEntity<String> getProvisioningFile(@PathVariable String macAddress) {
        return ResponseEntity.ok(provisioningService.getProvisioningFile(macAddress));
    }
}