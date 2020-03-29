package com.voverc.provisioning.service;

import com.voverc.provisioning.config.ProvisioningProperties;
import com.voverc.provisioning.exception.NotPresentedInDbException;
import com.voverc.provisioning.model.ConfigurationFileResponse;
import com.voverc.provisioning.model.Device;
import com.voverc.provisioning.repository.DeviceRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Nazar Lelyak.
 */
@ActiveProfiles("test")
@RunWith(MockitoJUnitRunner.class)
public class ProvisioningServiceImplTest {
    @Mock
    private DeviceRepository repository;

    private ProvisioningProperties properties;

    private ProvisioningService provisioningService;

    @Before
    public void init() {
        properties = ProvisioningProperties.builder()
                .domain("sip.test.com")
                .port(9999)
                .codec("G911")
                .codec("G922")
                .build();

        provisioningService = new ProvisioningServiceImpl(repository, properties);
    }

    @Test
    public void testThatForStoredDeviceConfigurationFileIsAutogeneratedFine() {
        Device device = Device.builder()
                .macAddress("mac-address-for-test")
                .username("harry")
                .password("potter")
                .build();

        ConfigurationFileResponse expectedResponse = ConfigurationFileResponse.builder()
                .username("harry")
                .password("potter")
                .domain(properties.getDomain())
                .port(properties.getPort())
                .codecs(properties.getCodecs())
                .build();

        when(repository.findById(notNull())).thenReturn(Optional.of(device));

        assertThat(provisioningService.getProvisioningFile("mac-address-for-test"), is(expectedResponse));
    }

    @Test
    public void testThatConfigurationFileIsAutogeneratedFineWithJsonFormatOverride() {
        Device device = Device.builder()
                .macAddress("mac-address-with-json-fragment")
                .username("ron")
                .password("weasley")
                .overrideFragment("{\"domain\":\"sip.anothertest.domain\",\"port\":\"7777\",\"timeout\":15}")
                .build();

        ConfigurationFileResponse expectedResponse = ConfigurationFileResponse.builder()
                .username("ron")
                .password("weasley")
                .domain("sip.anothertest.domain")
                .port(7777)
                .codecs(properties.getCodecs())
                .timeout(15)
                .build();

        when(repository.findById(notNull())).thenReturn(Optional.of(device));

        assertThat(provisioningService.getProvisioningFile("mac-address-with-json-fragment"), is(expectedResponse));
    }

    @Test
    public void testThatConfigurationFileIsAutogeneratedFineWithPropertiesFormatOverride() {
        Device device = Device.builder()
                .macAddress("mac-address-with-properties-fragment")
                .username("hermiona")
                .password("granger")
                .overrideFragment("domain=sip.propertiestest.domain\nport=9090\ntimeout=25")
                .build();

        ConfigurationFileResponse expectedResponse = ConfigurationFileResponse.builder()
                .username("hermiona")
                .password("granger")
                .domain("sip.propertiestest.domain")
                .port(9090)
                .codecs(properties.getCodecs())
                .timeout(25)
                .build();

        when(repository.findById(notNull())).thenReturn(Optional.of(device));

        assertThat(provisioningService.getProvisioningFile("mac-address-with-properties-fragment"), is(expectedResponse));
    }

    @Test(expected = IllegalArgumentException.class)
    public void ifMacAddressIsEmptyThrowException() {
        provisioningService.getProvisioningFile("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void ifMacAddressIsNullThrowException() {
        provisioningService.getProvisioningFile(null);
    }

    @Test(expected = NotPresentedInDbException.class)
    public void ifMacAddressIsWrongThrowException() {
        provisioningService.getProvisioningFile("mac-address-incorrect-path");
    }

    @Test
    public void configurationFilesShouldBeCachedByMacAddress() {
        Device device = Device.builder()
                .macAddress("a")
                .username("albus")
                .password("dumbledore")
                .overrideFragment("domain=sip.hogwarts.domain\nport=8989\ntimeout=42")
                .build();

        ConfigurationFileResponse expectedResponse = ConfigurationFileResponse.builder()
                .username("albus")
                .password("dumbledore")
                .domain("sip.hogwarts.domain")
                .port(8989)
                .codecs(properties.getCodecs())
                .timeout(42)
                .build();

        when(repository.findById("a")).thenReturn(Optional.of(device));

        assertThat(provisioningService.getProvisioningFile("a"), is(expectedResponse));
        assertThat(provisioningService.getProvisioningFile("a"), is(expectedResponse));
        assertThat(provisioningService.getProvisioningFile("a"), is(expectedResponse));

        verify(repository, times(1)).findById("a");
    }
}