package com.voverc.provisioning.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.voverc.provisioning.dto.FragmentDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

import static com.voverc.provisioning.dto.OverrideFragment.DOMAIN;
import static com.voverc.provisioning.dto.OverrideFragment.PORT;
import static com.voverc.provisioning.dto.OverrideFragment.TIMEOUT;

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

    public Optional<FragmentDTO> parseOverrideFragment() {
        if (overrideFragment == null || overrideFragment.isEmpty())
            return Optional.empty();

        return isPropertyFormat(overrideFragment) ?
                parsePropertiesFragment() :
                parseJsonFragment();
    }

    @SneakyThrows
    private Optional<FragmentDTO> parseJsonFragment() {
        ObjectMapper mapper = new ObjectMapper();
        return Optional.of(mapper.readValue(overrideFragment, FragmentDTO.class));
    }

    private Optional<FragmentDTO> parsePropertiesFragment() {
        FragmentDTO dto = new FragmentDTO();
        Stream<String> lines = Arrays.stream(overrideFragment.split("\n"));

        lines.forEach(line -> {
            if (line.contains(DOMAIN.getLowerCaseName())) {
                dto.setDomain(getPropertyValue(line));

            } else if (line.contains(PORT.getLowerCaseName())) {
                String port = getPropertyValue(line);
                dto.setPort(Integer.parseInt(port));

            } else if (line.contains(TIMEOUT.getLowerCaseName())) {
                String timeout = getPropertyValue(line);
                dto.setTimeout(Integer.parseInt(timeout));
            }
        });
        return Optional.of(dto);
    }

    private boolean isPropertyFormat(String line) {
        return line.contains("=");
    }

    private String getPropertyValue(String line) {
        String[] array = line.split("=");
        return array[1].trim();
    }
}