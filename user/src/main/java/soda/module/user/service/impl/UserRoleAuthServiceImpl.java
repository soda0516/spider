package soda.module.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import soda.module.core.web.util.JsonUtil;
import soda.module.user.entity.UserAuth;
import soda.module.user.entity.UserInfo;
import soda.module.user.entity.UserRole;
import soda.module.user.entity.UserRoleAuth;
import soda.module.user.mapper.UserAuthMapper;
import soda.module.user.mapper.UserRoleAuthMapper;
import soda.module.user.mapper.UserRoleMapper;
import soda.module.user.model.JwtUser;
import soda.module.user.model.JwtUserInfo;
import soda.module.user.service.IUserAuthService;
import soda.module.user.service.IUserInfoService;
import soda.module.user.service.IUserRoleAuthService;
import org.springframework.stereotype.Service;
import soda.module.user.service.IUserRoleService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jobob
 * @since 2019-07-17
 */
@Service
public class UserRoleAuthServiceImpl extends ServiceImpl<UserRoleAuthMapper, UserRoleAuth> implements IUserRoleAuthService {
    private final PasswordEncoder passwordEncoder;
    private final IUserInfoService iUserInfoService;
    private final UserRoleMapper userRoleMapper;
    private final UserRoleAuthMapper userRoleAuthMapper;
    private final UserAuthMapper userAuthMapper;
    @Autowired
    UserRoleAuthServiceImpl(PasswordEncoder passwordEncoder,
                            IUserInfoService iUserInfoService,
                            UserRoleMapper userRoleMapper,
                            UserRoleAuthMapper userRoleAuthMapper,
                            UserAuthMapper userAuthMapper){
        this.passwordEncoder = passwordEncoder;
        this.iUserInfoService = iUserInfoService;
        this.userRoleMapper = userRoleMapper;
        this.userRoleAuthMapper = userRoleAuthMapper;
        this.userAuthMapper = userAuthMapper;
    }
    @Override
    public boolean checkUserInfoByNameAndPassword(UserInfo userInfo) {
//        判空操作，放在controller层，以便返回一个Response结果
        UserInfo info = iUserInfoService.lambdaQuery()
                .eq(UserInfo::getUsername,userInfo.getUsername())
                .eq(UserInfo::getPassword,userInfo.getPassword())
                .one();
        if (null != info && userInfo.getUsername().equals(info.getUsername()) && userInfo.getPassword().equals(info.getPassword())){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public JwtUser getJwtUserInfo(UserInfo userInfo) {
        UserInfo info = iUserInfoService.lambdaQuery()
                .eq(UserInfo::getUsername,userInfo.getUsername())
                .eq(UserInfo::getPassword,userInfo.getPassword())
                .one();
        QueryWrapper<UserRole> userRoleQueryWrapper = new QueryWrapper<>();
        userRoleQueryWrapper.lambda().eq(UserRole::getId,info.getRoleId());
        UserRole userRole = userRoleMapper.selectOne(userRoleQueryWrapper);
        List<GrantedAuthority> authentication = new ArrayList<>();
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(userRole.getRoleName());
        authentication.add(simpleGrantedAuthority);

        return new JwtUser.Builder()
                .setId(info.getId())
                .setUsername(info.getUsername())
                .setAuthorities(authentication)
                .build();
    }
}
