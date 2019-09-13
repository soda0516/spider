package soda.module.user.controller;


import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import soda.module.core.web.exception.ForbiddenException;
import soda.module.core.web.response.ResponseBuilder;
import soda.module.core.web.response.ResponseModel;
import soda.module.core.web.util.JsonUtil;
import soda.module.user.constant.RedisConstant;
import soda.module.user.constant.ReturnMessage;
import soda.module.user.entity.UserInfo;
import soda.module.user.entity.UserRole;
import soda.module.user.model.JwtUser;
import soda.module.user.service.IUserInfoService;
import soda.module.user.service.IUserRoleService;
import soda.module.user.util.JwtUtil;
import soda.module.user.util.SecurityContextUtil;
import springfox.documentation.spring.web.json.Json;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jobob
 * @since 2019-07-17
 */
@Slf4j
@RestController
@RequestMapping("/user-info")
@Api(tags = "用户信息管理")
public class UserInfoController {

    private static Logger logger = LoggerFactory.getLogger(UserInfoController.class);
    @Value("${jwt.secret}")
    private String secret;

    private final IUserInfoService iUserInfoService;
    private final IUserRoleService iUserRoleService;
    private final RedisTemplate<String,Object> objectRedisTemplate;
    @Autowired
    UserInfoController(IUserInfoService iUserInfoService,
                       RedisTemplate<String,Object> objectRedisTemplate,
                       IUserRoleService iUserRoleService){
        this.iUserInfoService = iUserInfoService;
        this.objectRedisTemplate = objectRedisTemplate;
        this.iUserRoleService = iUserRoleService;
    }
    private Boolean userInfoNameIsExist(String name){
        if (iUserInfoService.lambdaQuery().eq(UserInfo::getUsername,name).list().size() > 0){
            return true;
        }else {
            return false;
        }
    }
    /**
     * 显示json格式的信息
     * @return
     */
    @GetMapping("/json")
    public ResponseModel showJson(){
        return ResponseBuilder.success(new UserInfo().setUsername("Json").setId(0).setPassword("123").setRoleId(0));
    }

    @GetMapping("/list")
    public ResponseModel<List<UserInfo>> getUserList(){
        return ResponseBuilder.success(iUserInfoService.list());
    }
    @GetMapping("/list/detail")
    public ResponseModel<List<UserInfo>> getUserInfoDetailList(){
        return ResponseBuilder.success(iUserInfoService.getUserInfoWithUserRole());
    }
    /**
     * 通过用户名查询一个信息
     * @param name
     * @return
     */
    @GetMapping("/name")
    public ResponseModel getInfoByName(@RequestParam(value = "name",required = false) String name){
        if (!userInfoNameIsExist(name)){
            return ResponseBuilder.failure(ReturnMessage.MESSAGE_NOTEXIST);
        }
        UserInfo userInfo = iUserInfoService.lambdaQuery().eq(UserInfo::getUsername,name).one();
        return ResponseBuilder.success(userInfo);
    }

    /**
     * 通过token查询一个用户的信息
     * @param token
     * @return
     */
    @GetMapping("/info")
    public ResponseModel<UserInfo> getUserInfoByToken(@RequestParam(value = "token",required = false) String token){
        int userIdFromJwtToken = JwtUtil.getUserIdFromJwtToken(secret,token);
        UserInfo info = iUserInfoService.getUserInfoWithUserRoleById(userIdFromJwtToken);
        if (null == info){
            throw new ForbiddenException("通过当前token，没有查询到相关用户的信息!");
        }
        return ResponseBuilder.success(info);
    }

    /**
     * 添加一条用户信息
     * @param data
     * @return
     */
    @PostMapping("")
    public ResponseModel addInfo(@RequestParam("info") String data){
        UserInfo userInfo = JsonUtil.json2Obj(data,UserInfo.class);
        if (userInfo == null || userInfo.getUsername() == null){
            return ResponseBuilder.failure(ReturnMessage.MESSAGE_ISNULL);
        }
        if (userInfoNameIsExist(userInfo.getUsername())){
            return ResponseBuilder.failure(ReturnMessage.MESSAGE_EXIST);
        }
        if (iUserInfoService.save(userInfo)){
            return ResponseBuilder.success();
        }else {
            return ResponseBuilder.failure(ReturnMessage.DB_OPERATE_ERROR);
        }
    }

    /**
     * 更新一条用户的信息
     * @param data
     * @return
     */
    @PutMapping("")
    public ResponseModel updateInfoById(@RequestParam("data") String data){
        UserInfo info = JsonUtil.json2Obj(data,UserInfo.class);
        if (info == null || info.getUsername() == null || info.getId() == null){
            return ResponseBuilder.failure(ReturnMessage.MESSAGE_ISNULL);
        }
//        if (userInfoNameIsExist(info.getUsername())){
//            return ResponseBuilder.failure(ReturnMessage.MESSAGE_EXIST);
//        }
        if (iUserInfoService.updateById(info)){
            return ResponseBuilder.success();
        }else {
            return ResponseBuilder.failure(ReturnMessage.DB_OPERATE_ERROR);
        }
    }

    /**
     * 删除一条用户信息
     * @param id
     * @return
     */
    @DeleteMapping("")
    public ResponseModel delInfoById(@RequestParam("id") String id){
        if (iUserInfoService.removeById(id)){
            return ResponseBuilder.success();
        }else {
            return ResponseBuilder.failure(ReturnMessage.DB_OPERATE_ERROR);
        }
    }

//    private void removeRedisTokenKeyByUserName(String userName){
//        Set<Object> keys = objectRedisTemplate.opsForHash().keys(RedisConstant.RedisTokenKey);
//        keys.forEach(value ->{
//            String key = value.toString();
//            String jwtUserName = JwtUtil.getUserNameFromJwtToken(secret,key);
////            如果当前修改的用户名与jwt字符串中解析出来的用户名相等，则说明当前用户进行了修改，所以要把相应的token删除掉
//            if (null!= jwtUserName && jwtUserName.equals(key)){
//                objectRedisTemplate.opsForHash().delete(RedisConstant.RedisTokenKey,key);
//            }
//        });
//    }
}
