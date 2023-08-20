package fun.fengwk.auth.rest;

import fun.fengwk.convention4j.springboot.test.starter.redis.EnableTestRedisServer;
import fun.fengwk.convention4j.springboot.test.starter.snowflake.EnableTestSnowflakeId;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author fengwk
 */
@EnableTestRedisServer
@EnableTestSnowflakeId
@SpringBootApplication
public class AuthRestTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthRestTestApplication.class, args);
    }

}
