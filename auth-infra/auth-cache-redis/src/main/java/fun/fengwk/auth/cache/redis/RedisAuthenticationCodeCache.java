package fun.fengwk.auth.cache.redis;

import fun.fengwk.auth.core.cache.AuthenticationCodeCache;
import fun.fengwk.auth.core.model.AuthenticationCodeBO;
import fun.fengwk.convention4j.common.gson.GsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author fengwk
 */
@Component
public class RedisAuthenticationCodeCache implements AuthenticationCodeCache {

    private static final String REDIS_KEY_AUTHENTICATION_CODE = "auth#authenticationCode#%s";

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public void set(AuthenticationCodeBO authenticationCodeBO, int expireSeconds) {
        if (authenticationCodeBO == null) {
            return;
        }
        redisTemplate.opsForValue().set(
                getCodeKey(authenticationCodeBO.getCode()),
                serialize(authenticationCodeBO),
                expireSeconds,
                TimeUnit.SECONDS);
    }

    @Override
    public AuthenticationCodeBO get(String code) {
        if (StringUtils.isEmpty(code)) {
            return null;
        }
        String serialized = redisTemplate.opsForValue().get(getCodeKey(code));
        return deserialize(serialized);
    }

    public AuthenticationCodeBO deserialize(String serialized) {
        if (fun.fengwk.convention4j.common.StringUtils.isEmpty(serialized)) {
            return null;
        }
        return GsonUtils.fromJson(serialized, AuthenticationCodeBO.class);
    }

    public String serialize(AuthenticationCodeBO authenticationCodeBO) {
        return GsonUtils.toJson(authenticationCodeBO);
    }

    private String getCodeKey(String code) {
        return String.format(REDIS_KEY_AUTHENTICATION_CODE, code);
    }

}
