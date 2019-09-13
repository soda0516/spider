package soda.module.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import soda.module.user.entity.UserAuth;
import soda.module.user.mapper.UserAuthMapper;
import soda.module.user.service.IUserAuthService;
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
public class UserAuthServiceImpl extends ServiceImpl<UserAuthMapper, UserAuth> implements IUserAuthService {

}
