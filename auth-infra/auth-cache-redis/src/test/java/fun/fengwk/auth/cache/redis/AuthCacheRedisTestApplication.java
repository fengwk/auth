package fun.fengwk.auth.cache.redis;

import fun.fengwk.auth.core.AuthCoreAutoConfiguration;
import fun.fengwk.convention4j.springboot.test.starter.redis.EnableTestRedisServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author fengwk
 */
@EnableTestRedisServer
@SpringBootApplication(exclude = AuthCoreAutoConfiguration.class)
public class AuthCacheRedisTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthCacheRedisTestApplication.class, args);
    }

}
