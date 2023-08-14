package fun.fengwk.auth.repo.mysql;

import fun.fengwk.auth.core.AuthCoreAutoConfiguration;
import fun.fengwk.convention4j.springboot.test.starter.snowflake.EnableTestSnowflakeId;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author fengwk
 */
@EnableTestSnowflakeId
@SpringBootApplication(exclude = AuthCoreAutoConfiguration.class)
public class AuthRepoMysqlTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthRepoMysqlTestApplication.class, args);
    }

}
