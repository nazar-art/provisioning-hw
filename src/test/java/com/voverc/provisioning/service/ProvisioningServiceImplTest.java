package com.voverc.provisioning.service;

import com.voverc.provisioning.config.ProvisioningProperties;
import com.voverc.provisioning.entity.Device;
import com.voverc.provisioning.exception.NotPresentedInDbException;
import com.voverc.provisioning.model.DeviceModel;
import com.voverc.provisioning.repository.DeviceRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
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

    @Mock
    private ProvisioningProperties properties;

    private ProvisioningService provisioningService;

    private Device testDevice = Device.builder()
            .macAddress("mac-address-for-test")
            .username("harry")
            .password("potter")
            .build();

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
    public void testThatConfigurationFileIsAutogeneratedFineForConferenceDevice() {
        testDevice.setModel(DeviceModel.CONFERENCE);
        testDevice.setOverrideFragment("{\"domain\":\"sip.domain.com\",\"port\":\"7777\",\"timeout\":19}");

        String expectedResponse = "{\"username\":\"harry\",\"password\":\"potter\",\"domain\":\"sip.domain.com\",\"port\":\"7777\",\"codecs\":\"[G911, G922]\",\"timeout\":\"19\"}";

        when(repository.findById(notNull())).thenReturn(Optional.of(testDevice));

        assertThat(provisioningService.getProvisioningFile("mac-address-for-test"), is(expectedResponse));
    }

    @Test
    public void testThatConfigurationFileIsAutogeneratedFineForDeskDevice() {
        testDevice.setModel(DeviceModel.DESK);
        testDevice.setOverrideFragment("domain=sip.propertiestest.domain\nport=9090\ntimeout=25");

        when(repository.findById(notNull())).thenReturn(Optional.of(testDevice));

        String expectedResponse = "username=harry\npassword=potter\ndomain=sip.propertiestest.domain\nport=9090\ncodecs=[G911, G922]\ntimeout=25";
        assertThat(provisioningService.getProvisioningFile("mac-address-for-test"), is(expectedResponse));
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
        testDevice.setMacAddress("a");
        testDevice.setModel(DeviceModel.DESK);

        when(repository.findById("a")).thenReturn(Optional.of(testDevice));

        String expectedResponse = "username=harry\npassword=potter\ndomain=sip.test.com\nport=9999\ncodecs=[G911, G922]";

        assertThat(provisioningService.getProvisioningFile("a"), is(expectedResponse));
        assertThat(provisioningService.getProvisioningFile("a"), is(expectedResponse));
        assertThat(provisioningService.getProvisioningFile("a"), is(expectedResponse));

        verify(repository, times(1)).findById("a");
    }
}