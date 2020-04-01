package com.voverc.provisioning.service;

import com.google.common.collect.Maps;
import com.voverc.provisioning.config.ProvisioningProperties;
import com.voverc.provisioning.dataprovider.BaseConfigurationDataProvider;
import com.voverc.provisioning.dataprovider.ConferenceConfigurationDP;
import com.voverc.provisioning.dataprovider.DeskConfigurationDP;
import com.voverc.provisioning.entity.Device;
import com.voverc.provisioning.exception.NotPresentedInDbException;
import com.voverc.provisioning.model.ProvisioningData;
import com.voverc.provisioning.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    private final Map<String, String> cache = Maps.newConcurrentMap();

    @Override
    @Transactional
    public String getProvisioningFile(String macAddress) {
        Assert.hasText(macAddress, "Mac address is empty!");

        if (cache.containsKey(macAddress))
            return cache.get(macAddress);

        Device device = deviceRepository.findById(macAddress)
                .orElseThrow(() -> new NotPresentedInDbException(macAddress));

        BaseConfigurationDataProvider dataProvider = getConfigurationProvider(device.getModel());
        ProvisioningData configuration = dataProvider.getConfiguration(device, provisioningProperties);

        String response = dataProvider.formatProvisioningData(configuration);

        cache.putIfAbsent(macAddress, response);
        return response;
    }

    private BaseConfigurationDataProvider getConfigurationProvider(Device.DeviceModel model) {
        if (model == Device.DeviceModel.DESK) {
            return new DeskConfigurationDP();

        } else if (model == Device.DeviceModel.CONFERENCE) {
            return new ConferenceConfigurationDP();
        }
        throw new RuntimeException("Unknown device model: " + model);
    }
}
