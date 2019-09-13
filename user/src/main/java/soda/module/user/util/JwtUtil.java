package soda.module.user.util;

import io.jsonwebtoken.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import soda.module.core.web.util.JsonUtil;
import soda.module.user.entity.UserAuth;
import soda.module.user.entity.UserInfo;
import soda.module.user.model.JwtStandardInfo;
import soda.module.user.model.JwtUser;
import soda.module.user.model.JwtUserInfo;
//import soda.module.user.model.UserInfo;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.time.Instant;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Describe
 * @Author orang
 * @Create 2019/4/14 12:53
 **/
public class JwtUtil {

    private JwtUtil(){}
    /**
     * 通过自定义一个key来进行生成签名，为的是为每一个用户单独设置一个秘钥，存取，通过redis来实现
     * @param secret
     * @return
     */
    public static String generateJwt(String secret, JwtStandardInfo jwtStandardInfo, JwtUser userInfo){
        //        如果没有用户信息，返回一个空的值,执行方法之前，先判断参数是否有效
        if (null == userInfo || null == secret){
            return null;
        }
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;
        Key key = generateKey(secret);
        JwtStandardInfo info = jwtStandardInfo;
        if (null == jwtStandardInfo){
            info = new JwtStandardInfo();
        }

        Instant instant = info.getIat().atZone(ZoneId.systemDefault()).toInstant();

        Map<String,Object> claims = new HashMap<>();
        // TODO: 2019/4/16 这里可以用来放入更多的用户信息，以供查询使用，后期可以修改成通过配置，泛型等形式，通过配置的方式进行修改。
        claims.put("userId",userInfo.getId());
        claims.put("username",userInfo.getUsername());
        List<String> grantedAuthorityList = new ArrayList<>();
        if (null != userInfo.getAuthorities()){
            grantedAuthorityList = userInfo.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        }

        claims.put("authority", grantedAuthorityList);

        JwtBuilder builder = Jwts.builder()
                .setClaims(claims)
                .setSubject(info.getSub())
                .setAudience(info.getAud())
                .setIssuedAt(Date.from(instant))
                .signWith(signatureAlgorithm,key);

        return builder.compact();
    }

    /**
     * 通过一个key来解析Jwt，为的就是控制jwt是否有效，注销、过期等情况，通过改变当前用户的key来控制。
     * @param secret
     * @return
     */
    public static Claims validateJwt(String secret,String token){
        Key key = generateKey(secret);
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            e.printStackTrace();
        } catch (UnsupportedJwtException e) {
            e.printStackTrace();
        } catch (MalformedJwtException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return claims;
    }
    /**
     * 根据缓存中自动生成的字符串，产生一个秘钥
     * @return
     */
    private static Key generateKey(String secret) {
        byte[] bytes = secret.getBytes();
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;
        Key key = new SecretKeySpec(bytes, signatureAlgorithm.getJcaName());
        return key;
    }

    /**
     * 通过秘钥，从token里面解析出来权限列表
     * @param secret
     * @param token
     * @return
     */
    public static List<GrantedAuthority> getAuthenticationListFromJwtToken(String secret,String token){
        Claims claims = validateJwt(secret,token);
//        如果解析不出来，肯定就直接返回一个null了
        if (null == claims){
            return null;
        }
        List<GrantedAuthority> authentication = new ArrayList<>();
        try {
            if (claims.get("authority").toString().length() > 2){
                List<String> authString = Arrays.asList(claims.get("authority").toString().substring(1,claims.get("authority").toString().length()-1));
                List<String> stringList = new ArrayList<>(authString);

                for (String auth:stringList
                     ) {
                    authentication.add(new SimpleGrantedAuthority(auth));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return authentication;
    }

    /**
     * 从token里面，直接解析出来用户名
     * @param secret
     * @param token
     * @return
     */
    public static String getUserNameFromJwtToken(String secret,String token){
        Claims claims = validateJwt(secret,token);
//        如果解析不出来，肯定就直接返回一个null了
        if (null == claims){
            return null;
        }
        String name = null;
        try {
            name = claims.get("username").toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return name;
    }
    public static Integer getUserIdFromJwtToken(String secret,String token){
        Claims claims = validateJwt(secret,token);
        Integer id = null;
//        如果解析不出来，肯定就直接返回一个null了
        if (null != claims && StringUtils.isNumeric(claims.get("userId").toString())){
            id = Integer.valueOf(claims.get("userId").toString());
        }
        return id;
    }
}
