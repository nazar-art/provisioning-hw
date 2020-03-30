package com.voverc.provisioning.controller;

import com.voverc.provisioning.model.ConfigurationFileResponse;
import com.voverc.provisioning.service.ProvisioningServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
        ConfigurationFileResponse expectedResponse = ConfigurationFileResponse.builder()
                .username("A")
                .password("password")
                .domain("test.domain.com")
                .port(1715)
                .codec("SO")
                .codec("GB")
                .timeout(5)
                .build();

        when(provisioningService.getProvisioningFile("aa-bb-cc")).thenReturn(expectedResponse);

        mockMvc.perform(get("/api/v1/provisioning/aa-bb-cc")
                .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value(expectedResponse.getUsername()))
                .andExpect(jsonPath("$.password").value(expectedResponse.getPassword()))
                .andExpect(jsonPath("$.domain").value(expectedResponse.getDomain()))
                .andExpect(jsonPath("$.port").value(expectedResponse.getPort()))
                .andExpect(jsonPath("$.codecs").isArray())
                .andExpect(jsonPath("$.codecs", hasSize(2)))
                .andExpect(jsonPath("$.codecs", hasItem(expectedResponse.getCodecs().get(0))))
                .andExpect(jsonPath("$.codecs", hasItem(expectedResponse.getCodecs().get(1))))
                .andExpect(jsonPath("$.timeout").value(expectedResponse.getTimeout()));
    }

    @Test
    public void ifMacAddressIsEmptyThrowException() throws Exception {
        mockMvc.perform(get("/api/v1/provisioning/")
                .accept(APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
