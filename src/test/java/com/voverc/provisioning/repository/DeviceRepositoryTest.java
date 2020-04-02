package com.voverc.provisioning.repository;

import com.voverc.provisioning.entity.Device;
import com.voverc.provisioning.model.DeviceModel;
import lombok.AllArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.*;

/**
 * @author Nazar Lelyak.
 */
@DataJpaTest
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
public class DeviceRepositoryTest {
    
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private DeviceRepository deviceRepository;

    @Test
    public void testGetDeviceByMacAddress() {
        Device expectedDevice = Device.builder()
                .macAddress("aa")
                .model(DeviceModel.CONFERENCE)
                .username("AA")
                .build();

        entityManager.persist(expectedDevice);

        Optional<Device> actualDevice = deviceRepository.findById("aa");

        assertEquals(Optional.of(expectedDevice), actualDevice);
    }

    @Test
    public void testDeviceNotFoundByMacAddress() {
        Optional<Device> device = deviceRepository.findById("hello");
        assertEquals(Optional.empty(), device);
    }
}