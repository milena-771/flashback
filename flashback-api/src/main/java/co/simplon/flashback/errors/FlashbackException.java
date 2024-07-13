package co.simplon.flashback.errors;

@SuppressWarnings("serial")
public class FlashbackException extends RuntimeException {

    private final String prefix = "ERR_";

    private final String code;

    public FlashbackException(String message, String code) {
	super(message);
	this.code = code;
    }

    public String getPrefix() {
	return prefix;
    }

    public String getCode() {
	return code;
    }

}
