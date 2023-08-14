package fun.fengwk.auth.core.manager;

import fun.fengwk.auth.core.facade.AuthenticationFacade;
import fun.fengwk.convention4j.common.NullSafe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author fengwk
 */
@Slf4j
@Component
public class AuthenticationManager {

    private final Map<String, AuthenticationFacade> authenticationFacadeMap;

    public AuthenticationManager(List<AuthenticationFacade> authenticationFacades) {
        this.authenticationFacadeMap = NullSafe.of(authenticationFacades).stream()
            .collect(Collectors.toMap(AuthenticationFacade::server, Function.identity()));
    }

    public List<String> listAuthenticationServers() {
        return new ArrayList<>(authenticationFacadeMap.keySet());
    }

    public AuthenticationFacade getAuthenticationFacade(String identity) {
        return authenticationFacadeMap.get(identity);
    }

}
