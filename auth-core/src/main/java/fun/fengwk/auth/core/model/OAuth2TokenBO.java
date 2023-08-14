package fun.fengwk.auth.core.model;

import fun.fengwk.auth.share.constant.TokenType;
import fun.fengwk.auth.share.model.OAuth2TokenDTO;
import fun.fengwk.convention4j.common.NullSafe;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

/**
 * @author fengwk
 */
@Data
public class OAuth2TokenBO {

    private Long id;
    private Long clientId;
    private String subjectId;
    private String scope;
    private TokenType tokenType;
    private String accessToken;
    private String refreshToken;
    private LocalDateTime lastRefreshTime; // 最后一次刷新的时间
    private LocalDateTime authorizationTime; // 授权的时间

    public static OAuth2TokenBO generate(Long id, Long clientId, String subjectId, String scope) {
        OAuth2TokenBO oauth2TokenBO = new OAuth2TokenBO();
        oauth2TokenBO.setId(id);
        oauth2TokenBO.setClientId(clientId);
        oauth2TokenBO.setSubjectId(subjectId);
        oauth2TokenBO.setScope(scope);
        oauth2TokenBO.setTokenType(TokenType.BEARER);
        oauth2TokenBO.setAccessToken(generateToken());
        oauth2TokenBO.setRefreshToken(generateToken());
        LocalDateTime now = LocalDateTime.now();
        oauth2TokenBO.setLastRefreshTime(now);
        oauth2TokenBO.setAuthorizationTime(now);
        return oauth2TokenBO;
    }

    private static String generateToken() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            sb.append(UUID.randomUUID().toString().replace("-", ""));
        }
        return sb.toString();
    }

    public Integer accessTokenExpiresIn(Integer accessTokenExpireSeconds) {
        if (accessTokenExpireSeconds == null) {
            return null;
        }
        LocalDateTime lastRefreshTime = getLastRefreshTime();
        LocalDateTime expiresTime = lastRefreshTime.plusSeconds(accessTokenExpireSeconds);
        return (int) ChronoUnit.SECONDS.between(LocalDateTime.now(), expiresTime);
    }

    public Integer refreshTokenExpiresIn(Integer refreshTokenExpireSeconds) {
        if (refreshTokenExpireSeconds == null) {
            return null;
        }
        LocalDateTime lastRefreshTime = getLastRefreshTime();
        LocalDateTime expiresTime = lastRefreshTime.plusSeconds(refreshTokenExpireSeconds);
        return (int) ChronoUnit.SECONDS.between(LocalDateTime.now(), expiresTime);
    }

    public Integer authorizationExpiresIn(Integer authorizationExpireSeconds) {
        if (authorizationExpireSeconds == null) {
            return null;
        }
        LocalDateTime authorizationTime = getAuthorizationTime();
        LocalDateTime expiresTime = authorizationTime.plusSeconds(authorizationExpireSeconds);
        return (int) ChronoUnit.SECONDS.between(LocalDateTime.now(), expiresTime);
    }

    public boolean accessTokenExpired(int accessTokenExpireSeconds) {
        return accessTokenExpiresIn(accessTokenExpireSeconds) <= 0;
    }

    public boolean refreshTokenExpired(int refreshTokenExpireSeconds) {
        return refreshTokenExpiresIn(refreshTokenExpireSeconds) <= 0;
    }

    public boolean authorizationExpired(int authorizationExpireSeconds) {
        return authorizationExpiresIn(authorizationExpireSeconds) <= 0;
    }

    public void refresh() {
        setAccessToken(generateToken());
        setRefreshToken(generateToken());
        setLastRefreshTime(LocalDateTime.now());
    }

    public OAuth2TokenDTO toDTO(Integer accessTokenExpireSeconds){
        OAuth2TokenDTO oauth2TokenDTO = new OAuth2TokenDTO();
        oauth2TokenDTO.setAccessToken(getAccessToken());
        oauth2TokenDTO.setTokenType(NullSafe.map(getTokenType(), TokenType::getCode));
        if (accessTokenExpireSeconds != null) {
            oauth2TokenDTO.setExpiresIn(accessTokenExpiresIn(accessTokenExpireSeconds));
        }
        oauth2TokenDTO.setRefreshToken(getRefreshToken());
        oauth2TokenDTO.setScope(getScope());
        return oauth2TokenDTO;
    }

}
