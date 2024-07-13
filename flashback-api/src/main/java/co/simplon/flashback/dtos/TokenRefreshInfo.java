package co.simplon.flashback.dtos;

public class TokenRefreshInfo {

    private String token;

    private String role;

    private String firstname;

    public TokenRefreshInfo() {
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

    @Override
    public String toString() {
	return " {token=" + token + ", role=" + role
		+ ", firstname=" + firstname + "}";
    }
}
