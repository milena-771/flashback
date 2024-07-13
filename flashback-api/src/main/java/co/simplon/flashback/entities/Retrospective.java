package co.simplon.flashback.entities;

import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "t_retrospectives")
public class Retrospective extends AbstractEntity {

    @Column(name = "retrospective_name")
    private String retrospectiveName;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "device_id")
    private Device device;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User organizer;

    @Column(name = "movies_number")
    private int moviesNumber;

    @Column(name = "participants_number")
    private int participantsNumber;

    public Retrospective() {
	// Required no-arg constructor
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

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public Device getDevice() {
	return device;
    }

    public void setDevice(Device device) {
	this.device = device;
    }

    public User getOrganizer() {
	return organizer;
    }

    public void setOrganizer(User user) {
	this.organizer = user;
    }

    public int getMoviesNumber() {
	return moviesNumber;
    }

    public void setMoviesNumber(int moviesNumber) {
	this.moviesNumber = moviesNumber;
    }

    public int getParticipantsNumber() {
	return participantsNumber;
    }

    public void setParticipantsNumber(
	    int participantsNumber) {
	this.participantsNumber = participantsNumber;
    }

    @Override
    public int hashCode() {
	return Objects.hash(retrospectiveName);
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if (obj instanceof Retrospective other) {
	    return Objects.equals(retrospectiveName,
		    other.retrospectiveName);
	}
	return false;
    }

    @Override
    public String toString() {
	return " {retrospectiveName=" + retrospectiveName
		+ ", startDate=" + startDate + ", endDate="
		+ endDate + ", description=" + description
		+ ", device=" + device + ", organizer="
		+ organizer + ", moviesNumber="
		+ moviesNumber + ", participantsNumber="
		+ participantsNumber + "}";
    }

}
