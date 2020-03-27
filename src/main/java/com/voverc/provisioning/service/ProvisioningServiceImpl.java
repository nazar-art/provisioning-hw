package com.voverc.provisioning.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.voverc.provisioning.config.ProvisioningProperties;
import com.voverc.provisioning.dto.FragmentDTO;
import com.voverc.provisioning.entity.ConfigurationFileResponse;
import com.voverc.provisioning.entity.Device;
import com.voverc.provisioning.exception.NotPresentedInDbException;
import com.voverc.provisioning.repository.DeviceRepository;
import com.voverc.provisioning.utils.ParserUtils;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.stream.Stream;

import static com.voverc.provisioning.dto.OverrideFragment.DOMAIN;
import static com.voverc.provisioning.dto.OverrideFragment.PORT;
import static com.voverc.provisioning.dto.OverrideFragment.TIMEOUT;
import static com.voverc.provisioning.utils.ParserUtils.*;


@Slf4j
@Service
@AllArgsConstructor
public class ProvisioningServiceImpl implements ProvisioningService {

    private DeviceRepository deviceRepository;
    private ProvisioningProperties provisioningProperties;

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

            String fragment = device.getOverrideFragment();
            FragmentDTO dto = (isPropertyFormat(fragment))
                    ? parsePropertiesFragment(fragment)
                    : parseJsonFragment(fragment);

            BeanUtils.copyProperties(dto, fileResponse);
        }

        log.debug("For MAC_ADDRESS: {}, CONFIGURATION_FILE: {}", macAddress, fileResponse);
        return fileResponse;
    }


    @SneakyThrows
    private FragmentDTO parseJsonFragment(String fragment) {
        log.debug("JSON_FORMAT_FRAGMENT: {}", fragment);

        ObjectMapper mapper = new ObjectMapper();
        FragmentDTO fragmentDTO = mapper.readValue(fragment, FragmentDTO.class);

        log.debug("PARSED_FRAGMENT: {}", fragmentDTO);
        return fragmentDTO;
    }

    private FragmentDTO parsePropertiesFragment(String fragment) {
        log.debug("PROPERTIES_FORMAT_FRAGMENT: {}", fragment);

        FragmentDTO fragmentDTO = new FragmentDTO();
        Stream<String> lines = Arrays.stream(fragment.split("\n"));

        lines.forEach(line -> {
            if (line.contains(DOMAIN.getLowerCaseName())) {
                fragmentDTO.setDomain(getPropertyValue(line));

            } else if (line.contains(PORT.getLowerCaseName())) {
                String port = getPropertyValue(line);
                fragmentDTO.setPort(Integer.parseInt(port));

            } else if (line.contains(TIMEOUT.getLowerCaseName())) {
                String timeout = getPropertyValue(line);
                fragmentDTO.setTimeout(Integer.parseInt(timeout));
            }
        });
        log.debug("PARSED_FRAGMENT: {}", fragmentDTO);
        return fragmentDTO;
    }
}
