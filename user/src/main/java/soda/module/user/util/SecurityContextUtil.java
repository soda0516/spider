package soda.module.user.util;

import org.springframework.security.core.context.SecurityContextHolder;
import soda.module.core.web.exception.UnAuthorizedException;
import soda.module.user.model.JwtUser;

/**
 * @Describe
 * @Author soda
 * @Create 2019/8/11 12:48
 **/
public class SecurityContextUtil {
    public static JwtUser getJwtUserFromContext(){
        JwtUser jwtUser;
        try {
            jwtUser = (JwtUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            throw new UnAuthorizedException("没查询到相关用户信息");
        }
        return jwtUser;
    }
}
