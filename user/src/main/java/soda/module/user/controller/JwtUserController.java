package soda.module.user.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import soda.module.core.web.response.ResponseBuilder;
import soda.module.core.web.response.ResponseModel;
import soda.module.core.web.util.JsonUtil;
import soda.module.user.constant.JwtDbConstant;
import soda.module.user.constant.ReturnMessage;
import soda.module.user.entity.UserInfo;
import soda.module.user.model.JwtStandardInfo;
import soda.module.user.model.JwtUser;
import soda.module.user.service.IJwtDbService;
import soda.module.user.service.IUserRoleAuthService;
import soda.module.user.util.JwtUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @Describe
 * @Author soda
 * @Create 2019/7/23 10:24
 **/
@RestController
@RequestMapping("/jwt-user")
@Api(tags = "用于jwt登录和登出的功能")
public class JwtUserController {

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.header}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Value("${jwt.x-token}")
    private String xToken;

    @Resource(name = JwtDbConstant.JWT_MYSQL_IMPL)
    IJwtDbService iJwtDbService;

    private final IUserRoleAuthService iUserRoleAuthService;

    JwtUserController(IUserRoleAuthService iUserRoleAuthService){
        this.iUserRoleAuthService = iUserRoleAuthService;
    }
    @GetMapping("/json")
    public ResponseModel showJson(){
        return ResponseBuilder.success(new UserInfo().setUsername("测试m"));
    }

    /**
     * 用户单击登录，如果成功返回一个jwt信息
     * @param data
     * @return
     */
    @PostMapping("/login")
    public ResponseModel login(@RequestParam("data") String data){
        UserInfo userInfo = JsonUtil.json2Obj(data,UserInfo.class);
        if (userInfo == null || userInfo.getUsername() == null || userInfo.getPassword() == null){
            return ResponseBuilder.warning(ReturnMessage.USERNAME_OR_PASSWORD_ISNULL);
        }
        if (iUserRoleAuthService.checkUserInfoByNameAndPassword(userInfo)){
            JwtStandardInfo jwtStandardInfo = new JwtStandardInfo();
            JwtUser jwtUserInfo = iUserRoleAuthService.getJwtUserInfo(userInfo);
            String token = JwtUtil.generateJwt(secret,jwtStandardInfo,jwtUserInfo);
//            添加token到redis缓存里面,添加之前，先得清除当前用户的其他缓存
            iJwtDbService.delJwtTokenInDbByUserName(jwtUserInfo.getUsername());
            iJwtDbService.addJwtTokenInDb(token);
            return ResponseBuilder.success(token);
        }else {
            return ResponseBuilder.warning(ReturnMessage.USERNAME_OR_PASSWORD_ERROR);
        }
    }

    @PostMapping("/logout")
    public ResponseModel login(HttpServletRequest httpRequest){
        String authHeader = httpRequest.getHeader(xToken);
        if (null == authHeader){
            return ResponseBuilder.failure();
        }
        String authString = authHeader.substring(tokenHead.length());
        iJwtDbService.delJwtTokenInDb(authString);
        return ResponseBuilder.success();
//        UserInfo userInfo = JsonUtil.json2Obj(data,UserInfo.class);
//
//        if (userInfo == null || userInfo.getUsername() == null || userInfo.getPassword() == null){
//            return ResponseBuilder.failure(ReturnMessage.USERNAME_OR_PASSWORD_ISNULL);
//        }
//        if (iUserRoleAuthService.checkUserInfoByNameAndPassword(userInfo)){
//            JwtStandardInfo jwtStandardInfo = new JwtStandardInfo();
//
//            JwtUser jwtUserInfo = iUserRoleAuthService.getJwtUserInfo(userInfo);
//            String token = JwtUtil.generateJwt(secret,jwtStandardInfo,jwtUserInfo);
////            添加token到redis缓存里面,添加之前，先得清除当前用户的其他缓存
//            jwtRedisUtil.delJwtTokenInRedisByUserName(jwtUserInfo.getUsername());
//            jwtRedisUtil.addJwtTokenInRedis(token);
//
//            return ResponseBuilder.success(token);
//        }else {
//            return ResponseBuilder.failure(ReturnMessage.USERNAME_OR_PASSWORD_ERROR);
//        }
    }
}
