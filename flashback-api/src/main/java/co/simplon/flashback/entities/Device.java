package co.simplon.flashback.entities;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "t_devices")
public class Device extends AbstractEntity {

    @Column(name = "device_name")
    private String deviceName;

    public Device() {
	// Required no-arg constructor
    }

    public String getDeviceName() {
	return deviceName;
    }

    public void setDeviceName(String deviceName) {
	this.deviceName = deviceName;
    }

    @Override
    public int hashCode() {
	return Objects.hash(deviceName);
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if (obj instanceof Device other) {
	    return Objects.equals(deviceName,
		    other.deviceName);
	}
	return false;
    }

    @Override
    public String toString() {
	return " {deviceName=" + deviceName + "}";
    }
}
