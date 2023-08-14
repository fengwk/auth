package fun.fengwk.auth.core.service;

import fun.fengwk.auth.core.facade.AuthenticationFacade;
import fun.fengwk.auth.core.manager.AuthenticationManager;
import fun.fengwk.auth.core.model.ClientBO;
import fun.fengwk.auth.core.model.OAuth2TokenBO;
import fun.fengwk.auth.core.repo.ClientRepository;
import fun.fengwk.auth.core.repo.OAuth2TokenRepository;
import fun.fengwk.auth.core.service.oauth2.AuthenticationCodeMode;
import fun.fengwk.auth.core.service.oauth2.ClientCredentialsMode;
import fun.fengwk.auth.core.service.oauth2.ImplicitMode;
import fun.fengwk.auth.core.service.oauth2.PasswordMode;
import fun.fengwk.auth.share.constant.AuthErrorCodes;
import fun.fengwk.auth.share.constant.GrantType;
import fun.fengwk.auth.share.constant.ResponseType;
import fun.fengwk.auth.share.model.OAuth2TokenDTO;
import fun.fengwk.convention4j.common.NullSafe;
import fun.fengwk.convention4j.common.gson.GsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.Map;

/**
 * @author fengwk
 */
@Slf4j
@Service
public class OAuth2Service {

    @Autowired
    private AuthenticationCodeMode authenticationCodeMode;
    @Autowired
    private ImplicitMode implicitMode;
    @Autowired
    private PasswordMode passwordMode;
    @Autowired
    private ClientCredentialsMode clientCredentialsMode;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private OAuth2TokenRepository oauth2TokenRepository;

    public URI authorize(
        @NotEmpty String responseType,
        @NotEmpty String clientId,
        @NotEmpty String redirectUri,
        String scope,
        String state,
        @NotNull Map<String, Object> authenticationInfo) {
        // 参数处理
        if (!NumberUtils.isCreatable(clientId)) {
            log.warn("invalid client id, responseType: {}, clientId: {}", responseType, clientId);
            throw AuthErrorCodes.INVALID_CLIENT_ID.asThrowable();
        }
        long cid = NumberUtils.toLong(clientId);

        // 客户端信息检查
        ClientBO clientBO = clientRepository.getById(cid);
        if (clientBO == null) {
            log.warn("client not found, responseType: {}, clientId: {}", responseType, clientId);
            throw AuthErrorCodes.CLIENT_NOT_FOUND.asThrowable();
        }

        // 根据responseType调用不同模式的处理逻辑
        ResponseType rt = ResponseType.of(responseType);
        if (rt == null) {
            log.warn("unknown responseType, responseType: {}", responseType);
            throw AuthErrorCodes.UNKNOWN_RESPONSE_TYPE.asThrowable();
        }
        switch (rt) {
            case CODE:
                return authenticationCodeMode.authorize(
                    rt, clientBO, redirectUri, scope, state, authenticationInfo);
            case TOKEN:
                return implicitMode.authorize(
                    rt, clientBO, redirectUri, scope, state, authenticationInfo);
            default:
                log.warn("unknown responseType, responseType: {}", responseType);
                throw AuthErrorCodes.UNKNOWN_RESPONSE_TYPE.asThrowable();
        }
    }

    public OAuth2TokenDTO token(
        @NotEmpty String grantType,
        String code,
        String redirectUri,
        String clientId,
        String clientSecret,
        String scope,
        String subjectId,
        String refreshToken,
        Map<String, Object> authenticationInfo) {
        // 参数处理
        if (!NumberUtils.isCreatable(clientId)) {
            log.warn("invalid client id, grantType: {}, clientId: {}", grantType, clientId);
            throw AuthErrorCodes.INVALID_CLIENT_ID.asThrowable();
        }
        long cid = NumberUtils.toLong(clientId);

        // 客户端信息检查
        ClientBO client = clientRepository.getById(cid);
        if (client == null) {
            log.warn("client not found, grantType: {}, clientId: {}", grantType, clientId);
            throw AuthErrorCodes.CLIENT_NOT_FOUND.asThrowable();
        }

        // 根据grantType调用不同模式的处理逻辑
        GrantType gt = GrantType.of(grantType);
        if (gt == null) {
            log.warn("unknown grant type, grantType: {}", grantType);
            throw AuthErrorCodes.UNKNOWN_GRANT_TYPE.asThrowable();
        }
        switch (gt) {
            case AUTHORIZATION_CODE:
                return authenticationCodeMode.token(
                    gt, code, redirectUri, client, clientSecret);
            case PASSWORD:
                return passwordMode.token(
                    gt, client, clientSecret, scope, authenticationInfo);
            case CLIENT_CREDENTIALS:
                return clientCredentialsMode.token(
                    gt, client, clientSecret, scope, subjectId);
            case REFRESH_TOKEN:
                return refreshToken(refreshToken, client);
            default:
                log.warn("unknown grantType, grantType: {}", grantType);
                throw AuthErrorCodes.UNKNOWN_GRANT_TYPE.asThrowable();
        }
    }

    public Map<String, Object> subject(String accessToken) {
        OAuth2TokenBO oauth2TokenBO = oauth2TokenRepository.getByAccessToken(accessToken);
        if (oauth2TokenBO == null) {
            log.warn("access token error, accessToken: {}", accessToken);
            throw AuthErrorCodes.ACCESS_TOKEN_ERROR.asThrowable();
        }

        ClientBO client = clientRepository.getById(oauth2TokenBO.getClientId());
        if (client == null) {
            log.warn("client not found, accessToken: {}, clientId: {}",
                accessToken, oauth2TokenBO.getClientId());
            throw AuthErrorCodes.CLIENT_NOT_FOUND.asThrowable();
        }

        if (oauth2TokenBO.accessTokenExpired(client.getRefreshTokenExpireSeconds())) {
            log.warn("accessToken expired, accessToken: {}", oauth2TokenBO.getAccessToken());
            throw AuthErrorCodes.ACCESS_TOKEN_EXPIRED.asThrowable();
        }
        if (oauth2TokenBO.authorizationExpired(client.getAuthorizationExpireSeconds())) {
            log.warn("authorization expired, refreshToken: {}", oauth2TokenBO.getRefreshToken());
            throw AuthErrorCodes.AUTHORIZATION_EXPIRED.asThrowable();
        }

        AuthenticationFacade authenticationFacade = authenticationManager
            .getAuthenticationFacade(client.getAuthenticationServer());
        if (authenticationFacade == null) {
            log.error("unknown authentication server, clientId: {}, authServerIdentity: {}",
                    client.getId(), client.getAuthenticationServer());
            throw AuthErrorCodes.UNKNOWN_AUTHENTICATION_SERVER.asThrowable();
        }

        Map<String, Object> subject = authenticationFacade.getSubject(
            oauth2TokenBO.getSubjectId(), oauth2TokenBO.getScope());
        return NullSafe.of(subject);
    }

    private OAuth2TokenDTO refreshToken(String refreshToken, ClientBO client) {
        OAuth2TokenBO oauth2TokenBO = oauth2TokenRepository.getByRefreshToken(refreshToken);
        if (oauth2TokenBO == null) {
            log.warn("refresh token error, refreshToken: {}", refreshToken);
            throw AuthErrorCodes.REFRESH_TOKEN_ERROR.asThrowable();
        }

        if (oauth2TokenBO.refreshTokenExpired(client.getRefreshTokenExpireSeconds())) {
            log.warn("refresh token expired, refreshToken: {}", oauth2TokenBO.getRefreshToken());
            throw AuthErrorCodes.REFRESH_TOKEN_EXPIRED.asThrowable();
        }
        if (oauth2TokenBO.authorizationExpired(client.getAuthorizationExpireSeconds())) {
            log.warn("authorization expired, refreshToken: {}", oauth2TokenBO.getRefreshToken());
            throw AuthErrorCodes.AUTHORIZATION_EXPIRED.asThrowable();
        }

        oauth2TokenBO.refresh();
        if (!oauth2TokenRepository.updateById(oauth2TokenBO)) {
            log.warn("token refresh failed, oauth2Token: {}", GsonUtils.toJson(oauth2TokenBO));
            throw AuthErrorCodes.TOKEN_REFRESH_FAILED.asThrowable();
        }

        return oauth2TokenBO.toDTO(client.getAccessTokenExpireSeconds());
    }

}
