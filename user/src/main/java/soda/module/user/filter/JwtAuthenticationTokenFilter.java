package soda.module.user.filter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import soda.module.core.web.exception.BusinessException;
import soda.module.user.constant.JwtDbConstant;
import soda.module.user.constant.RedisConstant;
import soda.module.user.model.JwtUser;
import soda.module.user.service.IJwtDbService;
import soda.module.user.util.JwtUtil;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @Describe
 * @Author soda
 * @Create 2019/7/23 18:44
 **/
@Component
// TODO: 2019/8/4 还需要增加的东西是，当员工信息更改的时候，则要遍历缓存中的token把，存有相关的用户名的token的key都移除掉（好麻烦，不过token里面最好不要存用户id）
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Resource(name = JwtDbConstant.JWT_MYSQL_IMPL)
    private IJwtDbService iJwtDbService;

    @Value("${jwt.header}")
    private String tokenHeader;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Value("${jwt.x-token}")
    private String xToken;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiryHours}")
    private Long expiryHours;

    private void putJwtUserToContext(Integer userId,String username,List<GrantedAuthority> authentication,HttpServletRequest httpServletRequest){
        JwtUser jwtUser = new JwtUser.Builder()
                .setId(userId)
                .setUsername(username)
                .setAuthorities(authentication).build();
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                jwtUser, null, jwtUser.getAuthorities());

        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }


    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String authString = httpServletRequest.getHeader(xToken);
        if (!StringUtils.isBlank(authString)) {
//            String authString = authHeader.substring(tokenHead.length());
//            先进性jwtToken有效性的验证，然后操作缓存
            Integer userId = JwtUtil.getUserIdFromJwtToken(secret,authString);
            String username = JwtUtil.getUserNameFromJwtToken(secret,authString);
            List<GrantedAuthority> authentication = JwtUtil.getAuthenticationListFromJwtToken(secret,authString);
//            && null == SecurityContextHolder.getContext()
            if (null!= userId && null != username && null != authentication) {
//                经过验证，说明jwtToken是有效的，首先判断缓存里是否存在相应jwtToken，如果存在还需要判断jwtToken是否过期，只有都通过了，才对SecurityContextHolder进行赋值权限
                if (iJwtDbService.isExistJwtTokenInDb(authString)){
                    LocalDateTime deadline =iJwtDbService.getTimeByJwtTokenInDb(authString);
//                    没有过期，刷新缓存中的jwtToken，同时对
                    if (null != deadline && LocalDateTime.now().isBefore(deadline)){
                        this.putJwtUserToContext(userId,username,authentication,httpServletRequest);
                        //刷新当前token的失效日期，同时进行下一步操作，过期了，就得清除相应的jwtToken
                        iJwtDbService.refreshJwtTokenInDb(authString);
                    }else {
//                        过期清除token
                        iJwtDbService.delJwtTokenInDb(authString);
                    }
                }
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
