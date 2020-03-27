package com.voverc.provisioning.controller;

import com.voverc.provisioning.entity.ConfigurationFileResponse;
import com.voverc.provisioning.service.ProvisioningServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
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
        ConfigurationFileResponse response = ConfigurationFileResponse.builder()
                .username("A")
                .password("password")
                .domain("test.domain.com")
                .port(1715)
                .codec("SO")
                .codec("GB")
                .timeout(5)
                .build();

        when(provisioningService.getProvisioningFile("aa-bb-cc")).thenReturn(response);

        mockMvc.perform(get("/api/v1/provisioning/aa-bb-cc")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.username").value("A"))
                .andExpect(jsonPath("$.password").value("password"))
                .andExpect(jsonPath("$.domain").value("test.domain.com"))
                .andExpect(jsonPath("$.port").value("1715"))
                .andExpect(jsonPath("$.codecs").isArray())
                .andExpect(jsonPath("$.codecs", hasSize(2)))
                .andExpect(jsonPath("$.codecs", hasItem("SO")))
                .andExpect(jsonPath("$.codecs", hasItem("GB")))
                .andExpect(jsonPath("$.timeout").value("5"));
    }
}
