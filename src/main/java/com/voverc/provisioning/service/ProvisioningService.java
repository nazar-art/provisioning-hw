package com.voverc.provisioning.service;

import com.voverc.provisioning.dto.FragmentDTO;
import com.voverc.provisioning.entity.ConfigurationFileResponse;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static com.voverc.provisioning.dto.OverrideFragment.*;

public interface ProvisioningService {

    ConfigurationFileResponse getProvisioningFile(String macAddress);

    default FragmentDTO parseConditionally(String fragment, Predicate<String> predicate, String splitter) {
        FragmentDTO fragmentDTO = new FragmentDTO();
        if (predicate.test(fragment)) {
            Stream<String> lines = Arrays.stream(fragment.split(splitter));

            lines.forEach(line -> {
                if (line.contains(DOMAIN.toString().toLowerCase())) {
                    Pattern pattern = Pattern.compile("([\\w.])*\\w+\\.\\w+");
                    Matcher matcher = pattern.matcher(line.trim());

                    if (matcher.find()) {
                        String group = matcher.group();
                        fragmentDTO.setDomain(group);
                    }
                } else if (line.contains(PORT.toString().toLowerCase())) {
                    String port = removeAllNonDigits(line);
                    fragmentDTO.setPort(Integer.parseInt(port));

                } else if (line.contains(TIMEOUT.toString().toLowerCase())) {
                    String timeout = removeAllNonDigits(line);
                    fragmentDTO.setTimeout(Integer.parseInt(timeout));
                }
            });
        }
        return fragmentDTO;
    }

    static String removeAllNonDigits(String line) {
        return line.replaceAll("[^0-9]", "").trim();
    }
}
