package soda.module.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import soda.module.user.entity.UserInfo;
import soda.module.user.mapper.UserInfoMapper;
import soda.module.user.service.IUserInfoService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jobob
 * @since 2019-07-17
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements IUserInfoService {
    @Override
    public List<UserInfo> getUserInfoWithUserRole() {
        return this.baseMapper.selectUserInfoWithUserRole();
    }

    @Override
    public UserInfo getUserInfoWithUserRoleById(int id) {
        return this.baseMapper.selectUserInfoWithUserRoleById(id);
    }
}
