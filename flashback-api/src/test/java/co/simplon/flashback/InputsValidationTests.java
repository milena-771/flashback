package co.simplon.flashback;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

public class InputsValidationTests extends BaseMvcTests {

    @DisplayName("should inputs be valid")
    @ParameterizedTest
    @CsvFileSource(resources = "/csv/validation/valid.csv", numLinesToSkip = 1, delimiter = DELIMITER, maxCharsPerColumn = MAX_CHARS_PER_COLUMN)
    void shouldBeValid(String method, String path,
	    String tokenName, String json)
	    throws Exception {
	perform(method, path, tokenName, json)
		.andExpect(status().is(204));

    }

    @DisplayName("should inputs be invalid")
    @ParameterizedTest
    @CsvFileSource(resources = "/csv/validation/invalid.csv", numLinesToSkip = 1, delimiter = DELIMITER, maxCharsPerColumn = MAX_CHARS_PER_COLUMN)
    void shouldBeInvalid(String method, String path,
	    String tokenName, String json)
	    throws Exception {
	perform(method, path, tokenName, json)
		.andExpect(status().is(400));

    }
}
