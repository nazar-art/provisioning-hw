package com.voverc.provisioning.service;

import com.google.common.collect.Maps;
import com.voverc.provisioning.config.ProvisioningProperties;
import com.voverc.provisioning.dataprovider.ConferenceConfigurationDP;
import com.voverc.provisioning.dataprovider.ConfigurationDataProvider;
import com.voverc.provisioning.dataprovider.DeskConfigurationDP;
import com.voverc.provisioning.entity.Device;
import com.voverc.provisioning.exception.NotPresentedInDbException;
import com.voverc.provisioning.model.DeviceModel;
import com.voverc.provisioning.model.ProvisioningData;
import com.voverc.provisioning.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class ProvisioningServiceImpl implements ProvisioningService {

    private final DeviceRepository deviceRepository;
    private final ProvisioningProperties provisioningProperties;

    private final Map<String, String> cache = Maps.newConcurrentMap();

    @Override
    @Transactional
    public String getProvisioningFile(String macAddress) {
        Assert.hasText(macAddress, "Mac address is empty!");

        if (cache.containsKey(macAddress))
            return cache.get(macAddress);

        Device device = deviceRepository.findById(macAddress)
                .orElseThrow(() -> new NotPresentedInDbException(macAddress));

        ConfigurationDataProvider dataProvider = getConfigurationProvider(device.getModel());
        ProvisioningData provisioningData = dataProvider.getConfiguration(device, provisioningProperties);

        String response = dataProvider.formatProvisioningData(provisioningData);

        cache.putIfAbsent(macAddress, response);
        return response;
    }

    private ConfigurationDataProvider getConfigurationProvider(DeviceModel model) {
        switch (model) {
            case DESK:
                return new DeskConfigurationDP();

            case CONFERENCE:
                return new ConferenceConfigurationDP();

            default:
                throw new RuntimeException("Unknown device model: " + model);
        }
    }
}
