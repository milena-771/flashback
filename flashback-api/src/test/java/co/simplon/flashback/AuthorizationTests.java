package co.simplon.flashback;

import static org.hamcrest.CoreMatchers.not;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

@DisplayName("Tests access endpoints")
class AuthorizationTests extends BaseMvcTests {

    @DisplayName("should be accepted")
    @ParameterizedTest
    @CsvFileSource(resources = "/csv/auth/accepted.csv", numLinesToSkip = 1, delimiter = DELIMITER, maxCharsPerColumn = MAX_CHARS_PER_COLUMN)
    void shouldBeAccepted(String method, String path,
	    String tokenName) throws Exception {
	perform(method, path, tokenName).andExpectAll(
		status().is(not(401)),
		status().is(not(403)));
    }

    @DisplayName("should be denied")
    @ParameterizedTest
    @CsvFileSource(resources = "/csv/auth/denied.csv", numLinesToSkip = 1, delimiter = DELIMITER, maxCharsPerColumn = MAX_CHARS_PER_COLUMN)
    void shouldBeDenied(String method, String path,
	    String tokenName, int expectedStatus)
	    throws Exception {
	perform(method, path, tokenName)
		.andExpect(status().is(expectedStatus));
    }

}
