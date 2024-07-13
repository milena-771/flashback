package co.simplon.flashback.entities;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "t_participants")
public class Participant extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "retrospective_id")
    private Retrospective retrospective;

    public Participant() {
	// Required no-arg constructor
    }

    public User getUser() {
	return user;
    }

    public void setUser(User user) {
	this.user = user;
    }

    public Retrospective getRetrospective() {
	return retrospective;
    }

    public void setRetrospective(
	    Retrospective retrospective) {
	this.retrospective = retrospective;
    }

    @Override
    public int hashCode() {
	return Objects.hash(retrospective, user);
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if (obj instanceof Participant other) {
	    return Objects.equals(retrospective,
		    other.retrospective)
		    && Objects.equals(user, other.user);
	}
	return false;
    }

    @Override
    public String toString() {
	return " {user=" + user + ", retrospective="
		+ retrospective + "}";
    }
}
