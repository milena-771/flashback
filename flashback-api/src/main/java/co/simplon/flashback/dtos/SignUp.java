package co.simplon.flashback.dtos;

import co.simplon.flashback.validation.UniqueEmail;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class SignUp {

    @NotBlank
    @Size(max = 50)
    @Pattern(regexp = "^[a-zA-Z-éàâèêôûîç'’ ]+$")
    private String firstname;

    @NotBlank
    @Size(max = 50)
    @Pattern(regexp = "^[a-zA-Z-éàâèêôûîç'’ ]+$")
    private String lastname;

    @NotBlank
    @UniqueEmail
    @Size(max = 100)
    @Pattern(regexp = "^[A-Za-z0-9-_.]+@[A-Za-z]+\\.{1}[A-Za-z]{2,4}")
    private String email;

    @NotBlank
    @Size(min = 8, max = 42)
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[%||!||*])(?!.* ).{8,42}")
    private String password;

    public SignUp() {
    }

    public String getFirstname() {
	return firstname;
    }

    public String getLastname() {
	return lastname;
    }

    public String getEmail() {
	return email;
    }

    public String getPassword() {
	return password;
    }

    @Override
    public String toString() {
	return " {firstname=" + firstname + ", lastname="
		+ lastname + ", email=" + email
		+ ", password=[protected] }";
    }

}
