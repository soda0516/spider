package soda.module.core.web.exception;


/**
 * 业务异常类
 */
public class BusinessException extends RuntimeException {
    public BusinessException() {
        super();
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

//    public BusinessException(BaseCodeMsg baseCodeMsg) {
//        super(baseCodeMsg.getMsg());
//    }
//
//    public BusinessException(CodeMsg codeMsg) {
//        super(codeMsg.getMsg());
//    }
}
