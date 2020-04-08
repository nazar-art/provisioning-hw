package com.voverc.provisioning.dataprovider;

import com.voverc.provisioning.model.CommonConfigurationKeys;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

import static com.voverc.provisioning.model.CommonConfigurationKeys.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Nazar Lelyak.
 */
@ActiveProfiles("test")
public class ConferenceConfigurationDPTest {

    private ConfigurationDataProvider dataProvider = new ConferenceConfigurationDP();

    @Test
    public void testParseOverrideFragment() {
        Map<String, String> actualResult = dataProvider.parseOverrideFragments("{\"domain\":\"sip.anotherdomain.com\",\"port\":\"5161\",\"timeout\":10}");

        assertNotNull(actualResult);
        assertEquals(3, actualResult.size());
        assertEquals("sip.anotherdomain.com", actualResult.get(DOMAIN.getLowerName()));
    }
}