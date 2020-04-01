package com.voverc.provisioning.controller;

import com.voverc.provisioning.service.ProvisioningServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * @author Nazar Lelyak.
 */
@ActiveProfiles("test")
@RunWith(MockitoJUnitRunner.class)
public class ProvisioningRestControllerTest {
    @Mock
    private ProvisioningServiceImpl provisioningService;

    private MockMvc mockMvc;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new ProvisioningController(provisioningService))
                .build();
    }

    @Test
    public void configurationFileIsReturnedForMacAddress() throws Exception {
        String expected = "Test content";

        when(provisioningService.getProvisioningFile("aa")).thenReturn(expected);

        mockMvc.perform(get("/api/v1/provisioning/aa")
                .accept(APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void ifMacAddressIsEmptyThrowException() throws Exception {
        mockMvc.perform(get("/api/v1/provisioning/")
                .accept(APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
