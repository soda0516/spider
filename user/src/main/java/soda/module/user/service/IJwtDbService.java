package soda.module.user.service;

import java.time.LocalDateTime;

/**
 * 用来进行jwt token 相关操作的接口
 */
public interface IJwtDbService {
    void addJwtTokenInDb(String token);
    void refreshJwtTokenInDb(String token);
    void delJwtTokenInDbByUserName(String username);
    void delJwtTokenInDb(String token);
    boolean isExistJwtTokenInDb(String token);
    LocalDateTime getTimeByJwtTokenInDb(String token);
}
