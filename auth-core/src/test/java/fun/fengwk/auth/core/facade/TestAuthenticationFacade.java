package fun.fengwk.auth.core.facade;

import fun.fengwk.convention4j.api.result.Result;
import fun.fengwk.convention4j.common.result.Results;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author fengwk
 */
@Component
public class TestAuthenticationFacade implements AuthenticationFacade {

    public static final String SERVER = "test_server";

    @Override
    public String server() {
        return "test_server";
    }

    @Override
    public Result<String> authenticate(Map<String, Object> authenticationInfo) {
        String username = (String) authenticationInfo.get("username");
        if (Objects.equals(username, "fengwk")) {
            return Results.ok("1");
        } else {
            return Results.ok(null);
        }
    }

    @Override
    public Result<List<String>> listScopes() {
        return Results.ok(Collections.singletonList("userInfo"));
    }

    @Override
    public Map<String, Object> getSubject(String subjectId, String scope) {
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("userId", 1L);
        userInfo.put("username", "fengwk");
        return userInfo;
    }

}
