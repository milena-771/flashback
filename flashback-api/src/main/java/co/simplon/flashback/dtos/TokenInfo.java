package co.simplon.flashback.dtos;

import java.time.LocalDateTime;

public class TokenInfo {

    private String token;

    private String role;

    private String firstname;

    private LocalDateTime exp;

    public TokenInfo() {
    }

    public String getToken() {
	return token;
    }

    public void setToken(String token) {
	this.token = token;
    }

    public String getRole() {
	return role;
    }

    public void setRole(String role) {
	this.role = role;
    }

    public String getFirstname() {
	return firstname;
    }

    public void setFirstname(String firstname) {
	this.firstname = firstname;
    }

    public LocalDateTime getExp() {
	return exp;
    }

    public void setExp(LocalDateTime exp) {
	this.exp = exp;
    }

    @Override
    public String toString() {
	return " {token=" + token + ", role=" + role
		+ ", firstname=" + firstname + ", exp="
		+ exp + "}";
    }

}
