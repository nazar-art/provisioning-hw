package com.voverc.provisioning.controller;

import com.voverc.provisioning.entity.ConfigurationFileResponse;
import com.voverc.provisioning.exception.NotPresentedInDbException;
import com.voverc.provisioning.service.ProvisioningServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
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
    public ResponseEntity notPresentedInDbHandler(NotPresentedInDbException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.badRequest().build();
    }
}