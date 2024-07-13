package co.simplon.flashback.dtos;

import java.time.LocalDate;

public class OrgaRetroDetails {

    private String retrospectiveName;

    private LocalDate startDate;

    private LocalDate endDate;

    private String deviceName;

    private int participantsNumber;

    private String description;

    public String getRetrospectiveName() {
	return retrospectiveName;
    }

    public void setRestrospectiveName(
	    String restrospectiveName) {
	this.retrospectiveName = restrospectiveName;
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

    public String getDeviceName() {
	return deviceName;
    }

    public void setDeviceName(String deviceName) {
	this.deviceName = deviceName;
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

    @Override
    public String toString() {
	return " {restrospectiveName=" + retrospectiveName
		+ ", startDate=" + startDate + ", endDate="
		+ endDate + ", deviceName=" + deviceName
		+ ", participantsNumber="
		+ participantsNumber + ", description="
		+ description + "}";
    }

}
