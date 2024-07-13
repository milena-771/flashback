package co.simplon.flashback.repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import co.simplon.flashback.dtos.DeviceDetails;
import co.simplon.flashback.entities.Device;

public interface DeviceRepository
	extends JpaRepository<Device, Long> {

    Collection<DeviceDetails> findAllProjectedByOrderByDeviceName();
}
