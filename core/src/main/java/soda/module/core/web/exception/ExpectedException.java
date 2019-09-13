package soda.module.core.web.exception;

/**
 * @Describe
 * @Author orang
 * @Create 2019/4/16 16:20
 **/
public abstract class ExpectedException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    public ExpectedException() {
    }
    public ExpectedException(String message) {
        super(message);
    }
    public ExpectedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExpectedException(Throwable cause) {
        super(cause);
    }

    public ExpectedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
