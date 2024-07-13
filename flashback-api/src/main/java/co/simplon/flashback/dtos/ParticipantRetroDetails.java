package co.simplon.flashback.dtos;

import java.time.LocalDate;

public class ParticipantRetroDetails {

    private String retrospectiveName;

    private LocalDate startDate;

    private LocalDate endDate;

    private String description;

    private String deviceName;

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
		+ endDate + ", description=" + description
		+ ", deviceName=" + deviceName + "}";
    }
}
