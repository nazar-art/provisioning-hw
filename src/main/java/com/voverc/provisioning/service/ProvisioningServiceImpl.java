package com.voverc.provisioning.service;

import com.voverc.provisioning.config.ProvisioningProperties;
import com.voverc.provisioning.entity.ConfigurationFileResponse;
import com.voverc.provisioning.entity.Device;
import com.voverc.provisioning.exception.NotPresentedInDbException;
import com.voverc.provisioning.repository.DeviceRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.Validate;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@Slf4j
@Service
@AllArgsConstructor
public class ProvisioningServiceImpl implements ProvisioningService {

    private DeviceRepository deviceRepository;
    private ProvisioningProperties provisioningProperties;

    @Override
    @Transactional
    public ConfigurationFileResponse getProvisioningFile(String macAddress) {
        Validate.notNull(macAddress, "mac address can not be null");

        Device device = deviceRepository.findById(macAddress)
                .orElseThrow(() -> new NotPresentedInDbException(macAddress));

        // standard provisioning flow
        ConfigurationFileResponse fileResponse = new ConfigurationFileResponse();
        BeanUtils.copyProperties(device, fileResponse);
        BeanUtils.copyProperties(provisioningProperties, fileResponse);

        // additional provisioning flow
        device.parseOverrideFragment()
                .ifPresent(dto -> BeanUtils.copyProperties(dto, fileResponse));

        return fileResponse;
    }
}
