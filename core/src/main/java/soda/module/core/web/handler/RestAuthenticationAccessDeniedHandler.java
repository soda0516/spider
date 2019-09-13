package soda.module.core.web.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import soda.module.core.web.response.ResponseBuilder;
import soda.module.core.web.util.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Describe
 * @Author soda
 * @Create 2019/8/4 21:39
 **/
@Component
public class RestAuthenticationAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        System.out.println("能进入到自定义AccessDeniedHandler处理程序");
        httpServletResponse.setContentType("application/json;charset=UTF-8");
//        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
        httpServletResponse.sendError(111);
        httpServletResponse.getWriter().write(JsonUtil.obj2Json(ResponseBuilder.failure("没有登录权限")));
    }
}
