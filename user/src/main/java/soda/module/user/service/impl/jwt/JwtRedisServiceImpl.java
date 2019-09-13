package soda.module.user.service.impl.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import soda.module.user.constant.JwtDbConstant;
import soda.module.user.constant.RedisConstant;
import soda.module.user.service.IJwtDbService;
import soda.module.user.util.JwtUtil;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * @Describe
 * @Author soda
 * @Create 2019/8/5 21:15
 **/
@Service(value = JwtDbConstant.JWT_REDIS_IMPL)
public class JwtRedisServiceImpl implements IJwtDbService {

    @Value("${jwt.expiryHours}")
    private Long expiryHours;

    @Value("${jwt.secret}")
    private String secret;

    private final RedisTemplate<String,Object> objectRedisTemplate;

    JwtRedisServiceImpl(RedisTemplate<String,Object> objectRedisTemplate){
        this.objectRedisTemplate = objectRedisTemplate;
    }

    /**
     * 添加jwt进入缓存，并返回一个boolean值
     * @param token
     * @return
     */
    public void addJwtTokenInDb(String token){
        objectRedisTemplate.opsForHash().put(RedisConstant.RedisTokenKey,token, LocalDateTime.now().plusHours(expiryHours));
    }

    /**
     * 刷新jwt进入缓存，并返回一个boolean值，其实跟添加是一样的 哈哈哈哈
     * @param token
     * @return
     */
    public void refreshJwtTokenInDb(String token){
        objectRedisTemplate.opsForHash().put(RedisConstant.RedisTokenKey,token, LocalDateTime.now().plusHours(expiryHours));
    }

    /**
     * 删除同一个username的，所有的jwtToken
     * @param username
     * @return
     */
    public void delJwtTokenInDbByUserName(String username){
        //                    说明缓存里面没有当前的jwtToken，需要清空当前用户的相关的jwtToken，确定缓存中没有当前用户的其他jwtToken
        Set<Object> objects = objectRedisTemplate.opsForHash().keys(RedisConstant.RedisTokenKey);
//                    清除缓存中当前用户名的jwtToken
        objects.forEach(value -> {
            String name = JwtUtil.getUserNameFromJwtToken(secret,value.toString());
            if (null != name && name.equals(username)){
                objectRedisTemplate.opsForHash().delete(RedisConstant.RedisTokenKey,value);
            }
        });
    }

    /**
     * 删除一个JwtToken
     * @param token
     */
    public void delJwtTokenInDb(String token){
        objectRedisTemplate.opsForHash().delete(RedisConstant.RedisTokenKey,token, LocalDateTime.now().plusHours(expiryHours));
    }

    /**
     * 判断缓存里面有没有这个token
     * @param token
     * @return
     */
    public boolean isExistJwtTokenInDb(String token){
        return objectRedisTemplate.opsForHash().hasKey(RedisConstant.RedisTokenKey,token);
    }

    /**
     * 从token里面那LocalDataTime弄出来
     * @param token
     * @return
     */
    public LocalDateTime getTimeByJwtTokenInDb(String token){
        return (LocalDateTime) objectRedisTemplate.opsForHash().get(RedisConstant.RedisTokenKey,token);
    }
}
