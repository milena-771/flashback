package co.simplon.flashback.entities;

import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "t_users")
public class User extends AbstractEntity {

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "email")
    private String email;

    @Column(name = "user_password")
    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @Column(name = "created_at")
    private LocalDate createdAt;

    public User() {
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

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public String getPassword() {
	return password;
    }

    public void setPassword(String password) {
	this.password = password;
    }

    public Role getRole() {
	return role;
    }

    public void setRole(Role role) {
	this.role = role;
    }

    public LocalDate getCreatedAt() {
	return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
	this.createdAt = createdAt;
    }

    @Override
    public int hashCode() {
	return Objects.hash(email);
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if (obj instanceof User other) {
	    return Objects.equals(email, other.email);
	}
	return false;
    }

    @Override
    public String toString() {
	return " {firstname=" + firstname + ", lastname="
		+ lastname + ", email=" + email
		+ ", password=[protected]" + ", role="
		+ role + ", createdAt=" + createdAt + "}";
    }
}
