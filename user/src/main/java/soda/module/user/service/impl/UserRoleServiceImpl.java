package soda.module.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import soda.module.user.entity.UserRole;
import soda.module.user.mapper.UserRoleMapper;
import soda.module.user.service.IUserRoleService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jobob
 * @since 2019-07-17
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {

}
