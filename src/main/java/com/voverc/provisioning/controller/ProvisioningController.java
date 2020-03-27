package com.voverc.provisioning.controller;

import com.voverc.provisioning.entity.ConfigurationFileResponse;
import com.voverc.provisioning.exception.NotPresentedInDbException;
import com.voverc.provisioning.service.ProvisioningServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@AllArgsConstructor
@RequestMapping("/api/v1")
public class ProvisioningController {

    private ProvisioningServiceImpl provisioningService;


    @GetMapping(value = "/provisioning/{macAddress}")
    public ResponseEntity<ConfigurationFileResponse> getProvisioningFile(@PathVariable String macAddress) {
        return ResponseEntity.ok(provisioningService.getProvisioningFile(macAddress));
    }

    /**
     * Exception Handlers:
     */
    @ExceptionHandler(NotPresentedInDbException.class)
    public ResponseEntity notPresentedInDbHandler() {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity incorrectArgumentHandler() {
        return ResponseEntity.badRequest().build();
    }
}