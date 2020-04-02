package com.voverc.provisioning.dataprovider;

import com.voverc.provisioning.model.CommonConfigurationKeys;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author Nazar Lelyak.
 */
@ActiveProfiles("test")
public class DeskConfigurationDataProviderTest {

    private ConfigurationDataProvider dataProvider = new DeskConfigurationDP();

    @Test
    public void testOverrideFragments() {
        Map<String, String> actualResult = dataProvider.parseOverrideFragments("domain=sip.anotherdomain.com\nport=5161\ntimeout=10");

        assertNotNull(actualResult);
        assertEquals(3, actualResult.size());
        assertEquals("5161", actualResult.get(CommonConfigurationKeys.PORT.getLowerName()));
    }
}