package fun.fengwk.auth.cache.redis;

import fun.fengwk.auth.core.cache.AuthenticationCodeCache;
import fun.fengwk.auth.core.model.AuthenticationCodeBO;
import fun.fengwk.auth.share.constant.ResponseType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Objects;

/**
 * @author fengwk
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AuthCacheRedisTestApplication.class)
public class RedisAuthenticationCodeCacheTest {

    @Autowired
    private AuthenticationCodeCache authenticationCodeCache;

    @Test
    public void test() {
        AuthenticationCodeBO authenticationCodeBO = AuthenticationCodeBO.generate(
                "1", ResponseType.CODE, 1L,
                "https://fengwk.fun/hpmepage", "userInfo", "123");
        authenticationCodeCache.set(authenticationCodeBO, 1000);
        AuthenticationCodeBO found = authenticationCodeCache.get(authenticationCodeBO.getCode());
        assert Objects.equals(authenticationCodeBO, found);
    }

}
