package me.zhengjie.modules.security.security;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.modules.security.service.dto.JwtUserDto;
import me.zhengjie.utils.RedisUtils;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * @author /
 */
@Slf4j
@ApplicationScoped
public class TokenProvider {
    @Context
    HttpHeaders headers;
    @Inject
    RedisUtils redisUtils;
    public static final String AUTHORITIES_UUID_KEY = "uid";
    public static final String AUTHORITIES_UID_KEY = "userId";
    /**
     * 依据Token 获取鉴权信息
     *
     * @param token /
     * @return /
     */
// fixme    Authentication getAuthentication(String token) {
//        Claims claims = getClaims(token);
//        User principal = new User(claims.getSubject(), "******", new ArrayList<>());
//        return new UsernamePasswordAuthenticationToken(principal, token, new ArrayList<>());
//    }
    //# token 续期检查时间范围（默认30分钟，单位毫秒），在token即将过期的一段时间内用户操作了，则给用户的token续期
    long detect = 1800000L;
    //  # 续期时间范围，默认1小时，单位毫秒
    long renew = 3600000L;

    /**
     * 创建Token 设置永不过期，
     * Token 的时间有效性转到Redis 维护
     *
     * @param user /
     * @return /
     */
    public String createToken(JwtUserDto user) {
        String r = RandomStringUtils.secureStrong().nextAlphanumeric(8);
        return UUID.randomUUID().toString().replace("-", "") + "-" + r;
    }

    /**
     * @param token 需要检查的token
     */
    public void checkRenewal(String token) {
        // 判断是否续期token,计算token的过期时间
        String loginKey = loginKey(token);
        long time = redisUtils.getExpire(loginKey) * 1000;
        Date expireDate = DateUtil.offset(new Date(), DateField.MILLISECOND, (int) time);
        // 判断当前时间与过期时间的时间差
        long differ = expireDate.getTime() - System.currentTimeMillis();
        // 如果在续期检查的范围内，则续期
        if (differ <= detect) {
            long renewVal = time + renew;
            redisUtils.expire(loginKey, renewVal, TimeUnit.MILLISECONDS);
        }
    }

    public String getToken() {
        String aaa = headers.getHeaderString("aaa");
        return aaa;
    }

    /**
     * 获取登录用户RedisKey
     * @param token /
     * @return key
     */
    public String loginKey(String token) {
        return "online_token:" + getId(token);
    }

    /**
     * 获取登录用户TokenKey
     * @param token /
     * @return /
     */
    public String getId(String token) {
        return token;
    }
}
