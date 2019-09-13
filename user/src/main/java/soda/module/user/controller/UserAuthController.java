package soda.module.user.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import soda.module.core.web.response.ResponseBuilder;
import soda.module.core.web.response.ResponseModel;
import soda.module.core.web.util.JsonUtil;
import soda.module.user.constant.ReturnMessage;
import soda.module.user.entity.UserAuth;
import soda.module.user.entity.UserRoleAuth;
import soda.module.user.service.IUserAuthService;
import soda.module.user.service.IUserRoleAuthService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jobob
 * @since 2019-07-17
 */
@RestController
@RequestMapping("/user-auth")
@Api(tags = "用户权限授权添加等功能")
public class UserAuthController {
    private final IUserAuthService iUserAuthService;
    private final IUserRoleAuthService iUserRoleAuthService;

    UserAuthController(IUserAuthService iUserAuthService,
                       IUserRoleAuthService iUserRoleAuthService){
        this.iUserAuthService = iUserAuthService;
        this.iUserRoleAuthService = iUserRoleAuthService;
    }
    @GetMapping("/json")
    public ResponseModel<UserAuth> showJson(){
        return ResponseBuilder.success(new UserAuth().setAuthName("测试").setId(1));
    }
    private Boolean userAuthIsNotNull(UserAuth userAuth){
//        判断输入的用户名json实体类是否为空，要是空的返回一个错误结果
        return (userAuth != null && userAuth.getAuthName() !=null);
    }
    private Boolean userAuthIsExist(UserAuth userAuth){
//        通过查询数据库，判断当前的用户名是否存在
        QueryWrapper<UserAuth> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserAuth::getAuthName,userAuth.getAuthName());
        return null != iUserAuthService.getMap(queryWrapper);
    }

    /**
     * 添加一条用户授权信息
     * @param data
     * @return
     */
    @PostMapping()
    public ResponseModel addAuth(@RequestParam("data") String data){
        UserAuth userAuth = JsonUtil.json2Obj(data, UserAuth.class);
        if (!userAuthIsNotNull(userAuth)){
            return ResponseBuilder.failure(ReturnMessage.MESSAGE_ISNULL);
        }
        if (userAuth != null &&userAuthIsExist(userAuth)){
            return ResponseBuilder.failure(ReturnMessage.MESSAGE_EXIST);
        }
//        如果不存在相同的用户名，则添加一条信息成功就返回成功，失败就返回失败
        if (iUserAuthService.save(userAuth)){
            return ResponseBuilder.success();
        }else {
            return ResponseBuilder.failure(ReturnMessage.DB_OPERATE_ERROR);
        }
    }
    /**
     * 修改一个用户权限信息
     * @param data
     * @return
     */
    @PutMapping()
    public ResponseModel updateAuth(@RequestParam("data") String data){
        UserAuth userAuth = JsonUtil.json2Obj(data,UserAuth.class);
        if (!userAuthIsNotNull(userAuth)){
            return ResponseBuilder.failure(ReturnMessage.MESSAGE_ISNULL);
        }
        if (userAuth != null &&userAuthIsExist(userAuth)){
            return ResponseBuilder.failure(ReturnMessage.MESSAGE_EXIST);
        }
        if (iUserAuthService.updateById(userAuth)){
            return ResponseBuilder.success();
        }else {
            return ResponseBuilder.failure(ReturnMessage.DB_OPERATE_ERROR);
        }
    }
    /**
     * 删除一个权限的信息
     * @param data
     * @return
     */
    @DeleteMapping()
    public ResponseModel delAuth(@RequestParam("data") String data){
        UserAuth userAuth = JsonUtil.json2Obj(data,UserAuth.class);
        if (userAuth == null || userAuth.getId() == null){
            return ResponseBuilder.failure(ReturnMessage.MESSAGE_ISNULL);
        }
        if (iUserAuthService.removeById(userAuth.getId())){
            return ResponseBuilder.success();
        }else {
            return ResponseBuilder.failure(ReturnMessage.DB_OPERATE_ERROR);
        }
    }

    /**
     * 查询所有的权限信息
     * @return
     */
    @GetMapping()
    public ResponseModel getAllAuth(){
        return ResponseBuilder.success(iUserAuthService.list());
    }

    /**
     *  通过角色id获得一个权限列表
     * @param roleId
     * @return
     */
    @GetMapping("/role")
    public ResponseModel<List<UserAuth>> getAuthListByRoleId(@RequestParam("id") int roleId){
        List<UserAuth> userAuthList = null;
        List<UserRoleAuth> roleAuthList = iUserRoleAuthService
                .lambdaQuery()
                .eq(UserRoleAuth::getRoleId,roleId)
                .list();
        if (roleAuthList.size() > 0){
            List<Integer> idList = roleAuthList
                    .stream()
                    .map(UserRoleAuth::getAuthId)
                    .collect(Collectors.toList());

            userAuthList = iUserAuthService
                    .lambdaQuery()
                    .in(UserAuth::getId,idList)
                    .list();
        }
        return ResponseBuilder.success(userAuthList);
    }

}
