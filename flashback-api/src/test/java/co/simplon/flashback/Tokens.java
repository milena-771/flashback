package co.simplon.flashback;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
class Tokens {

    /*
     * @Value("${skilltree.tests.fake-token}") private String fake;
     * 
     * @Value("${skilltree.tests.bad-secret-token}") private String badSecret;
     * 
     * @Value("${skilltree.tests.bad-issuer-token}") private String badIssuer;
     * 
     * @Value("${skilltree.tests.expired-token}") private String expired;
     */

    @Value("${flashback-api.tests.valid-admin-token}")
    private String admin;

    @Value("${flashback-api.tests.valid-user-token}")
    private String user;

    String get(final String name) {
	switch (name) {
	/*
	 * case "fake": return fake; case "badSecret": return badSecret; case
	 * "badIssuer": return badIssuer; case "expired": return expired;
	 */
	case "admin":
	    return admin;
	case "user":
	    return user;
	default:
	    throw new IllegalArgumentException(
		    "Unexpected value: " + name);
	}
    }

}
