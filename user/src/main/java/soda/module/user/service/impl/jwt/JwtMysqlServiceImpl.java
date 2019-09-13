package soda.module.user.service.impl.jwt;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import soda.module.user.constant.JwtDbConstant;
import soda.module.user.entity.UserToken;
import soda.module.user.mapper.UserTokenMapper;
import soda.module.user.service.IJwtDbService;
import soda.module.user.util.JwtUtil;

import java.time.LocalDateTime;

/**
 * @Describe
 * @Author soda
 * @Create 2019/8/9 10:21
 **/
@Service(value = JwtDbConstant.JWT_MYSQL_IMPL)
public class JwtMysqlServiceImpl implements IJwtDbService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiryHours}")
    private Long expiryHours;

    private final UserTokenMapper userTokenMapper;

    JwtMysqlServiceImpl(UserTokenMapper userTokenMapper){
        this.userTokenMapper = userTokenMapper;
    }

    @Override
    public void addJwtTokenInDb(String token) {
        String username = JwtUtil.getUserNameFromJwtToken(secret,token);
        UserToken userToken = new UserToken();
        userToken.setJwtToken(token);
        userToken.setUserName(username);
        userToken.setExpireTime(LocalDateTime.now().plusHours(expiryHours));
        userTokenMapper.insert(userToken);
    }

    @Override
    public void refreshJwtTokenInDb(String token) {
        QueryWrapper<UserToken> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserToken::getJwtToken,token);
        UserToken userToken = userTokenMapper.selectOne(queryWrapper);
        userToken.setExpireTime(LocalDateTime.now().plusHours(expiryHours));
        userTokenMapper.updateById(userToken);
    }

    @Override
    public void delJwtTokenInDbByUserName(String username) {
        QueryWrapper<UserToken> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserToken::getUserName,username);
        userTokenMapper.delete(queryWrapper);
    }

    @Override
    public void delJwtTokenInDb(String token) {
        QueryWrapper<UserToken> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserToken::getJwtToken,token);
        userTokenMapper.delete(queryWrapper);
    }

    @Override
    public boolean isExistJwtTokenInDb(String token) {
        QueryWrapper<UserToken> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserToken::getJwtToken,token);
        return userTokenMapper.selectCount(queryWrapper) > 0;
    }

    @Override
    public LocalDateTime getTimeByJwtTokenInDb(String token) {
        QueryWrapper<UserToken> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserToken::getJwtToken,token);
        UserToken userToken = userTokenMapper.selectOne(queryWrapper);
        return userToken.getExpireTime();
    }
}
