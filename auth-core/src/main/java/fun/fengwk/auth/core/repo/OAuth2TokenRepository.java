package fun.fengwk.auth.core.repo;

import fun.fengwk.auth.core.model.OAuth2TokenBO;

/**
 * @author fengwk
 */
public interface OAuth2TokenRepository {

    Long generateId();

    boolean add(OAuth2TokenBO oauth2TokenBO);

    boolean updateById(OAuth2TokenBO oauth2TokenBO);

    OAuth2TokenBO getByAccessToken(String accessToken);

    OAuth2TokenBO getByRefreshToken(String refreshToken);

}
