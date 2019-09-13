package soda.module.core.web.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import soda.module.core.web.exception.*;
import soda.module.core.web.response.ResponseBuilder;
import soda.module.core.web.response.ResponseModel;

/**
 * @Describe
 * @Author soda
 * @Create 2019/7/18 14:15
 **/
@RestControllerAdvice
public class WebExceptionHandler {
    private static Logger logger = LoggerFactory.getLogger(WebExceptionHandler.class);
    @ExceptionHandler(BusinessException.class)
    public ResponseModel businessException(BusinessException e){
        logger.warn(e.toString());
        return ResponseBuilder.warning(e.getMessage());
    }
    @ExceptionHandler(ForbiddenException.class)
    public ResponseModel forbiddenException(ForbiddenException e){
        logger.error(e.toString());
        return ResponseBuilder.forbidden("没有当前操作的权限！");
    }
    @ExceptionHandler(JsonConverterException.class)
    public ResponseModel jsonConverterException(JsonConverterException e){
        logger.error(e.toString());
        return ResponseBuilder.failure("Json转换错误！");
    }

    @ExceptionHandler(MultipartUploadException.class)
    public ResponseModel multipartUploadException(MultipartUploadException e){
        logger.error(e.toString());
        return ResponseBuilder.failure("上传错误！");
    }

    @ExceptionHandler(OptimisticLockException.class)
    public ResponseModel optimisticLockException(OptimisticLockException e){
        logger.error(e.toString());
        return ResponseBuilder.failure("优先锁错误！");
    }

    @ExceptionHandler(SystemException.class)
    public ResponseModel systemException(SystemException e){
        logger.error(e.toString());
        return ResponseBuilder.failure("系统错误！");
    }

    @ExceptionHandler(UnAuthorizedException.class)
    public ResponseModel unAuthorizedException(UnAuthorizedException e){
        logger.error(e.toString());
        return ResponseBuilder.forbidden("未授权错误！");
    }

    @ExceptionHandler(WebUtilsException.class)
    public ResponseModel webUtilsException(WebUtilsException e){
        logger.error(e.toString());
        return ResponseBuilder.failure("Web工具出现错误！");
    }
    @ExceptionHandler(Exception.class)
    public ResponseModel exception(Exception e){
        logger.error(e.toString());
        return ResponseBuilder.failure("未知没有捕获到的错误！");

    }

}
