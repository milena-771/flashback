package co.simplon.flashback.entities;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "t_directors")
public class Director extends AbstractEntity {

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "director_code")
    private String directorCode;

    public Director() {
	// Required no-arg constructor
    }

    public String getFirstname() {
	return firstname;
    }

    public void setFirstname(String firstname) {
	this.firstname = firstname;
    }

    public String getLastname() {
	return lastname;
    }

    public void setLastname(String lastname) {
	this.lastname = lastname;
    }

    public String getDirectorCode() {
	return directorCode;
    }

    public void setDirectorCode(String directorCode) {
	this.directorCode = directorCode;
    }

    @Override
    public int hashCode() {
	return Objects.hash(directorCode);
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if (obj instanceof Director other) {
	    return Objects.equals(directorCode,
		    other.directorCode);
	}
	return false;
    }

    @Override
    public String toString() {
	return " {firstname=" + firstname + ", lastname="
		+ lastname + ", directorCode="
		+ directorCode + "}";
    }

}
