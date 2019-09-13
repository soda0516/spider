package soda.module.user.model;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Describe
 * @Author orang
 * @Create 2019/4/14 16:03
 **/
@Data
public class JwtStandardInfo {
    public JwtStandardInfo(){
        this.iss = "iss";
        this.sub = "sub";
        this.aud = "aud";
//        不设置默认的过期时间，这个系统架构里面过期时间写在redis里面
        this.exp = "";
        this.nbf = "nbf";
//        签发时间
        this.iat = LocalDateTime.now();
        this.jti = "jti";
    }
    private String iss;
    private String sub;
    private String aud;
    private String exp;
    private String nbf;
    private LocalDateTime iat;
    private String jti;
}
