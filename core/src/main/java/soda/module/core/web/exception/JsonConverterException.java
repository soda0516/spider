package soda.module.core.web.exception;

public class JsonConverterException extends ExpectedException {

	private static final long serialVersionUID = 1L;

	public JsonConverterException() {
    }

    public JsonConverterException(String message) {
        super(message);
    }

    public JsonConverterException(String message, Throwable cause) {
        super(message, cause);
    }

    public JsonConverterException(Throwable cause) {
        super(cause);
    }

    protected JsonConverterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
