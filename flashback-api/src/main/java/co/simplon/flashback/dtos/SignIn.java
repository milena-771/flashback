package co.simplon.flashback.dtos;

import jakarta.validation.constraints.NotBlank;

public class SignIn {

    @NotBlank
    private String email;

    @NotBlank
    private String password;

    public SignIn() {
    }

    public String getEmail() {
	return email;
    }

    public String getPassword() {
	return password;
    }

    @Override
    public String toString() {
	return " {email=" + email
		+ ", password=[protected] }";
    }

}
