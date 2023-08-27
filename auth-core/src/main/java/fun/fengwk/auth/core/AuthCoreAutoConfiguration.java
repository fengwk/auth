package fun.fengwk.auth.core;

import fun.fengwk.auth.core.facade.ClientFacade;
import fun.fengwk.auth.core.facade.DefaultClientFacade;
import fun.fengwk.auth.core.repo.ClientRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author fengwk
 */
@ComponentScan
@Configuration
public class AuthCoreAutoConfiguration {

    @ConditionalOnMissingBean
    @Bean
    public ClientFacade defaultClientFacade(ClientRepository clientRepository) {
        return new DefaultClientFacade(clientRepository);
    }

}
