package soda.module.user.controller;


import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import soda.module.core.web.response.ResponseBuilder;
import soda.module.core.web.response.ResponseModel;
import soda.module.core.web.util.JsonUtil;
import soda.module.user.constant.ReturnMessage;
import soda.module.user.entity.UserRoleAuth;
import soda.module.user.service.IUserRoleAuthService;
import soda.module.user.service.IUserRoleService;

import javax.validation.constraints.Null;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jobob
 * @since 2019-07-17
 */
@RestController
@RequestMapping("/user-role-auth")
@Api(tags = "用户的权限和角色对应关系的管理")
public class UserRoleAuthController {
    private final IUserRoleAuthService iUserRoleAuthService;
    @Autowired
    UserRoleAuthController(IUserRoleAuthService iUserRoleAuthService){
        this.iUserRoleAuthService = iUserRoleAuthService;
    }
    @GetMapping("/json")
    public ResponseModel showJson(){
        return ResponseBuilder.success(new UserRoleAuth().setAuthId(0).setAuthId(0).setRoleId(0));
    }

    /**
     * 添加一条信息
     * @param data
     * @return
     */
    @PostMapping()
    public ResponseModel add(@RequestParam("data") String data){
        UserRoleAuth userRoleAuth = JsonUtil.json2Obj(data,UserRoleAuth.class);
        if (userRoleAuth == null || userRoleAuth.getRoleId() == null || userRoleAuth.getAuthId() == null){
            return ResponseBuilder.failure(ReturnMessage.MESSAGE_ISNULL);
        }
        List<UserRoleAuth> userRoleAuthList = iUserRoleAuthService.lambdaQuery().eq(UserRoleAuth::getRoleId,userRoleAuth.getRoleId()).eq(UserRoleAuth::getAuthId,userRoleAuth.getAuthId()).list();
        if (userRoleAuthList.size() > 0){
            return ResponseBuilder.failure(ReturnMessage.MESSAGE_EXIST);
        }
        if (iUserRoleAuthService.save(userRoleAuth)){
            return ResponseBuilder.success();
        }else {
            return ResponseBuilder.failure(ReturnMessage.DB_OPERATE_ERROR);
        }
    }

    /**
     * 更新一条信息
     * @param data
     * @return
     */
    @PutMapping()
    public ResponseModel update(@RequestParam("data") String data){
        UserRoleAuth userRoleAuth = JsonUtil.json2Obj(data,UserRoleAuth.class);
        if (userRoleAuth == null || userRoleAuth.getRoleId() == null || userRoleAuth.getAuthId() == null){
            return ResponseBuilder.failure(ReturnMessage.MESSAGE_ISNULL);
        }
        if (iUserRoleAuthService.lambdaQuery().eq(UserRoleAuth::getRoleId,userRoleAuth.getRoleId()).eq(UserRoleAuth::getAuthId,userRoleAuth.getAuthId()).list().size() > 0){
            return ResponseBuilder.failure(ReturnMessage.MESSAGE_EXIST);
        }
        if (iUserRoleAuthService.updateById(userRoleAuth)){
            return ResponseBuilder.success();
        }else {
            return ResponseBuilder.failure(ReturnMessage.DB_OPERATE_ERROR);
        }
    }

    /**
     * 删除一条信息
     * @param id
     * @return
     */
    @DeleteMapping()
    public ResponseModel del(@RequestParam("id") String id){
        if (iUserRoleAuthService.removeById(id)){
            return ResponseBuilder.success();
        }else {
            return ResponseBuilder.failure(ReturnMessage.DB_OPERATE_ERROR);
        }
    }
}