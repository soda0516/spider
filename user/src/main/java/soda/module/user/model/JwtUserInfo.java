package soda.module.user.model;

import lombok.Data;
import soda.module.user.entity.UserAuth;
import soda.module.user.entity.UserInfo;
import soda.module.user.entity.UserRole;

import java.util.List;

/**
 * @Describe
 * @Author soda
 * @Create 2019/7/23 10:15
 **/
@Data
public class JwtUserInfo {
    private UserInfo userInfo;
    private UserRole userRole;
    private List<UserAuth> userAuthList;
}
