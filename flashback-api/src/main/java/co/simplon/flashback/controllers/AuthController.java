package co.simplon.flashback.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.simplon.flashback.dtos.SignIn;
import co.simplon.flashback.dtos.SignUp;
import co.simplon.flashback.dtos.TokenInfo;
import co.simplon.flashback.dtos.TokenRefreshInfo;
import co.simplon.flashback.services.UserService;
import jakarta.validation.Valid;

@RestController
public class AuthController {

    private UserService service;

    public AuthController(UserService service) {
	this.service = service;
    }

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void signUp(@RequestBody @Valid SignUp inputs) {
	service.signUp(inputs);
    }

    @PostMapping("/sign-in")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public TokenInfo signIn(@RequestBody SignIn inputs) {
	return service.signIn(inputs);
    }

    @GetMapping("/refresh-token")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public TokenRefreshInfo refreshToken() {
	return service.refresh();
    }

}
