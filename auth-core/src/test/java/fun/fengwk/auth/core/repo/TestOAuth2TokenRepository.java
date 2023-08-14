package fun.fengwk.auth.core.repo;

import fun.fengwk.auth.core.model.OAuth2TokenBO;
import fun.fengwk.convention4j.common.gson.GsonUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author fengwk
 */
@Component
public class TestOAuth2TokenRepository implements OAuth2TokenRepository {

    private long id;
    private final Map<Long, OAuth2TokenBO> map = new HashMap<>();

    @Override
    public Long generateId() {
        return ++id;
    }

    @Override
    public boolean add(OAuth2TokenBO oauth2TokenBO) {
        oauth2TokenBO = GsonUtils.fromJson(GsonUtils.toJson(oauth2TokenBO), OAuth2TokenBO.class);
        return map.put(oauth2TokenBO.getId(), oauth2TokenBO) == null;
    }

    @Override
    public boolean updateById(OAuth2TokenBO oauth2TokenBO) {
        oauth2TokenBO = GsonUtils.fromJson(GsonUtils.toJson(oauth2TokenBO), OAuth2TokenBO.class);
        map.put(oauth2TokenBO.getId(), oauth2TokenBO);
        return true;
    }

    @Override
    public OAuth2TokenBO getByAccessToken(String accessToken) {
        OAuth2TokenBO oauth2TokenBO = map.values().stream()
                .filter(t -> Objects.equals(accessToken, t.getAccessToken()))
                .findFirst()
                .orElse(null);
        return GsonUtils.fromJson(GsonUtils.toJson(oauth2TokenBO), OAuth2TokenBO.class);
    }

    @Override
    public OAuth2TokenBO getByRefreshToken(String refreshToken) {
        OAuth2TokenBO oauth2TokenBO = map.values().stream()
                .filter(t -> Objects.equals(refreshToken, t.getRefreshToken()))
                .findFirst()
                .orElse(null);
        return GsonUtils.fromJson(GsonUtils.toJson(oauth2TokenBO), OAuth2TokenBO.class);
    }

}
