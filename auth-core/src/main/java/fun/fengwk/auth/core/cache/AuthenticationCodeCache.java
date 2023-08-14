package fun.fengwk.auth.core.cache;

import fun.fengwk.auth.core.model.AuthenticationCodeBO;

/**
 * @author fengwk
 */
public interface AuthenticationCodeCache {

    void set(AuthenticationCodeBO authenticationCodeBO, int expireSeconds);

    AuthenticationCodeBO get(String code);

}
