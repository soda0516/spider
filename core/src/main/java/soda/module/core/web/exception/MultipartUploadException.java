package soda.module.core.web.exception;

public class MultipartUploadException extends ExpectedException {
   
	private static final long serialVersionUID = 1L;

	public MultipartUploadException() {
    }

    public MultipartUploadException(String message) {
        super(message);
    }

    public MultipartUploadException(String message, Throwable cause) {
        super(message, cause);
    }

    public MultipartUploadException(Throwable cause) {
        super(cause);
    }

    protected MultipartUploadException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
