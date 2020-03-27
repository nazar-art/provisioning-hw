package com.voverc.provisioning.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Nazar Lelyak.
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ProvisioningControllerSystemTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void configurationFileAreGeneratedWithTestProfilesData() throws Exception {
        this.mockMvc.perform(get("/api/v1/provisioning/aa-bb-cc-dd-ee-ff")
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().string(allOf(
                        containsString("{\"username\":\"john\",\"password\":\"doe\",\"domain\":\"sip.test.com\",\"port\":6090,\"codecs\":[\"G911\",\"G922\"],\"timeout\":0}")
                )));
    }

    @Test
    public void configurationFileAreAvailableWithPropertiesOverrideFragment() throws Exception {
        this.mockMvc.perform(get("/api/v1/provisioning/a1-b2-c3-d4-e5-f6")
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().string(allOf(
                        containsString("{\"username\":\"walter\",\"password\":\"white\",\"domain\":\"sip.anotherdomain.com\",\"port\":5161,\"codecs\":[\"G911\",\"G922\"],\"timeout\":10}")
                )));
    }

    @Test
    public void configurationFileAreAvailableWithJsonOverrideFragment() throws Exception {
        this.mockMvc.perform(get("/api/v1/provisioning/1a-2b-3c-4d-5e-6f")
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().string(allOf(
                        containsString("{\"username\":\"eric\",\"password\":\"blue\",\"domain\":\"sip.anotherdomain.com\",\"port\":5161,\"codecs\":[\"G911\",\"G922\"],\"timeout\":10}")
                )));
    }

    @Test
    public void ifMacAddressIsWrongThrowException() throws Exception {
        mockMvc.perform(get("/api/v1/provisioning/wrong-mac-address")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}