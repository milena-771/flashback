package co.simplon.flashback.dtos;

import java.time.LocalDate;

import co.simplon.flashback.entities.Device;

public class RetroDetailsForUpdate {

    private String retrospectiveName;

    private LocalDate startDate;

    private LocalDate endDate;

    private Device device;

    private String deviceName;

    private int participantsNumber;

    private String description;

    public RetroDetailsForUpdate() {
    }

    public String getRetrospectiveName() {
	return retrospectiveName;
    }

    public void setRetrospectiveName(
	    String retrospectiveName) {
	this.retrospectiveName = retrospectiveName;
    }

    public LocalDate getStartDate() {
	return startDate;
    }

    public void setStartDate(LocalDate startDate) {
	this.startDate = startDate;
    }

    public LocalDate getEndDate() {
	return endDate;
    }

    public void setEndDate(LocalDate endDate) {
	this.endDate = endDate;
    }

    public Device getDevice() {
	return device;
    }

    public void setDevice(Device device) {
	this.device = device;
    }

    public int getParticipantsNumber() {
	return participantsNumber;
    }

    public void setParticipantsNumber(
	    int participantsNumber) {
	this.participantsNumber = participantsNumber;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public String getDeviceName() {
	return deviceName;
    }

    public void setDeviceName(String deviceName) {
	this.deviceName = deviceName;
    }

    @Override
    public String toString() {
	return " {retrospectiveName=" + retrospectiveName
		+ ", startDate=" + startDate + ", endDate="
		+ endDate + ", device=" + device
		+ ", deviceName=" + deviceName
		+ ", participantsNumber="
		+ participantsNumber + ", description="
		+ description + "}";
    }
}
