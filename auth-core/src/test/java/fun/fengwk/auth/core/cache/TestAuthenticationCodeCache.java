package fun.fengwk.auth.core.cache;

import fun.fengwk.auth.core.model.AuthenticationCodeBO;
import fun.fengwk.convention4j.common.gson.GsonUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author fengwk
 */
@Component
public class TestAuthenticationCodeCache implements AuthenticationCodeCache {

    private final Map<String, AuthenticationCodeBO> map = new HashMap<>();

    @Override
    public void set(AuthenticationCodeBO authenticationCodeBO, int expireSeconds) {
        authenticationCodeBO = GsonUtils.fromJson(GsonUtils.toJson(authenticationCodeBO), AuthenticationCodeBO.class);
        map.put(authenticationCodeBO.getCode(), authenticationCodeBO);
    }

    @Override
    public AuthenticationCodeBO get(String code) {
        return map.get(code);
    }

}
