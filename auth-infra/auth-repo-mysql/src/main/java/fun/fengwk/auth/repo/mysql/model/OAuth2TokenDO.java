package fun.fengwk.auth.repo.mysql.model;

import fun.fengwk.auth.core.model.OAuth2TokenBO;
import fun.fengwk.auth.share.constant.TokenType;
import fun.fengwk.convention4j.common.NullSafe;
import fun.fengwk.convention4j.springboot.starter.cache.annotation.Key;
import fun.fengwk.convention4j.springboot.starter.cache.mapper.BaseCacheDO;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author fengwk
 */
@Data
public class OAuth2TokenDO extends BaseCacheDO<Long> {

    private Long clientId;
    private String subjectId;
    private String scope;
    private String tokenType;
    @Key
    private String accessToken;
    @Key
    private String refreshToken;
    private LocalDateTime lastRefreshTime;
    private LocalDateTime authorizationTime;

    public static OAuth2TokenDO bo2do(OAuth2TokenBO oauth2TokenBO) {
        if (oauth2TokenBO == null) {
            return null;
        }
        OAuth2TokenDO oauth2TokenDO = new OAuth2TokenDO();
        oauth2TokenDO.setId(oauth2TokenBO.getId());
        oauth2TokenDO.setClientId(oauth2TokenBO.getClientId());
        oauth2TokenDO.setSubjectId(oauth2TokenBO.getSubjectId());
        oauth2TokenDO.setScope(oauth2TokenBO.getScope());
        oauth2TokenDO.setTokenType(NullSafe.map(oauth2TokenBO.getTokenType(), TokenType::getCode));
        oauth2TokenDO.setAccessToken(oauth2TokenBO.getAccessToken());
        oauth2TokenDO.setRefreshToken(oauth2TokenBO.getRefreshToken());
        oauth2TokenDO.setLastRefreshTime(oauth2TokenBO.getLastRefreshTime());
        oauth2TokenDO.setAuthorizationTime(oauth2TokenBO.getAuthorizationTime());
        return oauth2TokenDO;
    }

    public OAuth2TokenBO toBO() {
        OAuth2TokenBO oAuth2TokenBO = new OAuth2TokenBO();
        oAuth2TokenBO.setId(getId());
        oAuth2TokenBO.setClientId(getClientId());
        oAuth2TokenBO.setSubjectId(getSubjectId());
        oAuth2TokenBO.setScope(getScope());
        oAuth2TokenBO.setTokenType(NullSafe.map(getTokenType(), TokenType::of));
        oAuth2TokenBO.setAccessToken(getAccessToken());
        oAuth2TokenBO.setRefreshToken(getRefreshToken());
        oAuth2TokenBO.setLastRefreshTime(getLastRefreshTime());
        oAuth2TokenBO.setAuthorizationTime(getAuthorizationTime());
        return oAuth2TokenBO;
    }

}
