package com.voverc.provisioning.service;

import com.google.common.collect.Maps;
import com.voverc.provisioning.config.ProvisioningProperties;
import com.voverc.provisioning.entity.ConfigurationFileResponse;
import com.voverc.provisioning.entity.Device;
import com.voverc.provisioning.exception.NotPresentedInDbException;
import com.voverc.provisioning.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.Map;


@Slf4j
@Service
@RequiredArgsConstructor
public class ProvisioningServiceImpl implements ProvisioningService {

    private final DeviceRepository deviceRepository;
    private final ProvisioningProperties provisioningProperties;

    private final Map<String, ConfigurationFileResponse> cache = Maps.newConcurrentMap();

    @Override
    @Transactional
    public ConfigurationFileResponse getProvisioningFile(String macAddress) {
        Assert.hasText(macAddress, "Mac address is empty!");

        if (cache.containsKey(macAddress))
            return cache.get(macAddress);

        Device device = deviceRepository.findById(macAddress)
                .orElseThrow(() -> new NotPresentedInDbException(macAddress));

        // standard provisioning flow
        ConfigurationFileResponse fileResponse = new ConfigurationFileResponse();
        BeanUtils.copyProperties(device, fileResponse);
        BeanUtils.copyProperties(provisioningProperties, fileResponse);

        // additional provisioning flow
        device.parseOverrideFragment()
                .ifPresent(dto -> BeanUtils.copyProperties(dto, fileResponse));

        cache.putIfAbsent(macAddress, fileResponse);
        return fileResponse;
    }
}
