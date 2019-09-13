package soda.module.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Repository;
import soda.module.user.entity.UserInfo;
import soda.module.user.entity.UserRoleAuth;
import soda.module.user.model.JwtUser;
import soda.module.user.model.JwtUserInfo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jobob
 * @since 2019-07-17
 */
public interface IUserRoleAuthService extends IService<UserRoleAuth> {
    boolean checkUserInfoByNameAndPassword(UserInfo userInfo);
    JwtUser getJwtUserInfo(UserInfo userInfo);
}
