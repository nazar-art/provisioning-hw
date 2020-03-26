package com.voverc.provisioning.service;

import com.voverc.provisioning.config.ProvisioningProperties;
import com.voverc.provisioning.dto.FragmentDTO;
import com.voverc.provisioning.entity.ConfigurationFileResponse;
import com.voverc.provisioning.entity.Device;
import com.voverc.provisioning.exception.NotPresentedInDbException;
import com.voverc.provisioning.repository.DeviceRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@AllArgsConstructor
public class ProvisioningServiceImpl implements ProvisioningService {

    private ProvisioningProperties provisioningProperties;
    private DeviceRepository deviceRepository;

    @Override
    public ConfigurationFileResponse getProvisioningFile(String macAddress) {

        Device device = deviceRepository.findById(macAddress)
                .orElseThrow(() -> new NotPresentedInDbException(macAddress));

        // standard provisioning flow
        ConfigurationFileResponse fileResponse = new ConfigurationFileResponse();
        BeanUtils.copyProperties(device, fileResponse);
        BeanUtils.copyProperties(provisioningProperties, fileResponse);

        // additional provisioning flow
        if (device.getOverrideFragment() != null) {

            // check if format is json or property
            String fragment = device.getOverrideFragment();
            FragmentDTO dto = (fragment.contains("=")) ? parsePropertiesFormat(fragment) : parseJsonFormat(fragment);

            BeanUtils.copyProperties(dto, fileResponse);
        }

        return fileResponse;
    }

    private FragmentDTO parseJsonFormat(String fragment) {
        return parseConditionally(fragment, p -> p.contains(":"), ",");
    }

    private FragmentDTO parsePropertiesFormat(String fragment) {
        return parseConditionally(fragment, p -> p.contains("="), "\n");
    }
}
