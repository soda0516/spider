package soda.module.user.controller;


import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import soda.module.core.web.response.ResponseBuilder;
import soda.module.core.web.response.ResponseModel;
import soda.module.core.web.util.JsonUtil;
import soda.module.user.constant.ReturnMessage;
import soda.module.user.entity.UserRole;
import soda.module.user.service.IUserRoleService;

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
@RequestMapping("/user-role")
@Api(tags = "用户角色管理")
public class UserRoleController {
    private final IUserRoleService iUserRoleService;
    @Autowired
    UserRoleController(IUserRoleService iUserRoleService){
        this.iUserRoleService = iUserRoleService;
    }
    @GetMapping("/json")
    @PreAuthorize("hasAnyRole('TEST01','TEST02')")
    public ResponseModel showJson(){
        return ResponseBuilder.success(new UserRole().setId(1).setRoleName("ROLE_TEST").setRoleNote("测试管理员"));
    }

    /**
     * 添加一个用户角色
     * @param data
     * @return
     */
    @PostMapping()
    public ResponseModel addRole(@RequestParam("data") String data){
        UserRole userRole = JsonUtil.json2Obj(data,UserRole.class);
        if (userRole == null || userRole.getRoleName() == null || userRole.getRoleNote() == null){
            return ResponseBuilder.failure(ReturnMessage.MESSAGE_ISNULL);
        }
        List<UserRole> roleList = iUserRoleService.lambdaQuery().eq(UserRole::getRoleName,userRole.getRoleName()).or().eq(UserRole::getRoleNote,userRole.getRoleNote()).list();
        if (roleList.size() > 0){
            return ResponseBuilder.failure(ReturnMessage.MESSAGE_EXIST);
        }
        if (iUserRoleService.save(userRole)){
            return ResponseBuilder.success();
        }else {
            return ResponseBuilder.failure(ReturnMessage.DB_OPERATE_ERROR);
        }
    }
    /**
     * 修改一个用户角色的信息
     */
    @PutMapping()
    public ResponseModel updateRole(@RequestParam("data") String data){
        UserRole userRole = JsonUtil.json2Obj(data,UserRole.class);
        if (userRole == null || userRole.getRoleName() == null || userRole.getRoleNote() == null){
            return ResponseBuilder.failure(ReturnMessage.MESSAGE_ISNULL);
        }
        if (iUserRoleService.lambdaQuery().eq(UserRole::getRoleName,userRole.getRoleName()).or().eq(UserRole::getRoleNote,userRole.getRoleNote()).list().size() > 0){
            return ResponseBuilder.failure(ReturnMessage.MESSAGE_EXIST);
        }
        if (iUserRoleService.updateById(userRole)){
            return ResponseBuilder.success();
        }else {
            return ResponseBuilder.failure(ReturnMessage.DB_OPERATE_ERROR);
        }
    }

    /**
     * 删除一个角色信息
     * @param id
     * @return
     */
    @DeleteMapping()
    public ResponseModel delRole(@RequestParam("id") String id){
        if (iUserRoleService.removeById(id)){
            return ResponseBuilder.success();
        }else {
            return ResponseBuilder.failure(ReturnMessage.DB_OPERATE_ERROR);
        }
    }

    /**
     * 获得所有的角色列表
     * @return
     */
    @GetMapping()
    public ResponseModel getAll(){
        return ResponseBuilder.success(iUserRoleService.list());
    }
}
