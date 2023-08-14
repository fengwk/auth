package fun.fengwk.auth.core.service.oauth2;

import fun.fengwk.auth.core.facade.AuthenticationFacade;
import fun.fengwk.auth.core.manager.AuthenticationManager;
import fun.fengwk.auth.core.model.ClientBO;
import fun.fengwk.auth.core.model.OAuth2TokenBO;
import fun.fengwk.auth.core.repo.OAuth2TokenRepository;
import fun.fengwk.auth.share.constant.AuthErrorCodes;
import fun.fengwk.auth.share.constant.GrantType;
import fun.fengwk.auth.share.constant.OAuth2Mode;
import fun.fengwk.auth.share.constant.ResponseType;
import fun.fengwk.auth.share.model.OAuth2TokenDTO;
import fun.fengwk.convention4j.api.result.Result;
import fun.fengwk.convention4j.common.NullSafe;
import fun.fengwk.convention4j.common.StringUtils;
import fun.fengwk.convention4j.common.gson.GsonUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * @author fengwk
 */
@Slf4j
public abstract class BaseOAuth2Mode {

    @Autowired
    protected AuthenticationManager authenticationManager;
    @Autowired
    protected OAuth2TokenRepository oauth2TokenRepository;

    protected abstract OAuth2Mode mode();

    protected void checkResponseType(ResponseType responseType, ClientBO client) {
        OAuth2Mode mode = mode();
        if (!mode.validateResponseType(responseType)) {
            throw new IllegalArgumentException("responseType must be " + mode.getResponseType());
        }
        if (!client.supportedResponseType(responseType)) {
            log.warn("client unsupported responseType, responseType: {}, clientId: {}",
                responseType, client.getId());
            throw AuthErrorCodes.CLIENT_UNSUPPORTED_RESPONSE_TYPE.asThrowable();
        }
    }

    protected HttpUrl checkRedirectUri(String redirectUri, ClientBO client) {
        HttpUrl rUri = HttpUrl.parse(redirectUri);
        if (rUri == null) {
            log.warn("client unsupported redirectUri, redirectUri: {}",
                redirectUri);
            throw AuthErrorCodes.REDIRECT_URI_ERROR.asThrowable();
        }
        if (!client.supportedRedirectUri(redirectUri)) {
            log.warn("client unsupported redirectUri, clientId: {}, redirectUri: {}",
                client.getId(), redirectUri);
            throw AuthErrorCodes.CLIENT_UNSUPPORTED_REDIRECT_URI.asThrowable();
        }
        return rUri;
    }

    protected void checkScope(String scope, ClientBO client) {
        if (StringUtils.isNotEmpty(scope)) {
            AuthenticationFacade authenticationServer = authenticationManager
                .getAuthenticationFacade(client.getAuthenticationServer());
            if (authenticationServer == null) {
                log.error("unknown authentication server, clientId: {}, authServerIdentity: {}",
                    client.getId(), client.getAuthenticationServer());
                throw AuthErrorCodes.UNKNOWN_AUTHENTICATION_SERVER.asThrowable();
            }

            Result<List<String>> result = authenticationServer.listScopes();
            if (!result.isSuccess()) {
                log.error("listScopes error, authenticationServer: {}",
                    client.getAuthenticationServer());
                throw AuthErrorCodes.AUTHENTICATE_ERROR.asThrowable();
            }

            if (!NullSafe.of(result.getData()).contains(scope)) {
                log.warn("authentication server unsupported scope, clientId: {}, authenticationServer: {}, scope: {}",
                    client.getId(), client.getAuthenticationServer(), scope);
                throw AuthErrorCodes.AUTHENTICATION_UNSUPPORTED_SCOPE.asThrowable();
            }
        }
    }

    protected String authenticate(Map<String, Object> authenticationInfo, ClientBO client) {
        AuthenticationFacade authenticationFacade = authenticationManager
            .getAuthenticationFacade(client.getAuthenticationServer());
        if (authenticationFacade == null) {
            log.error("unknown authentication server, clientId: {}, authServerIdentity: {}",
                client.getId(), client.getAuthenticationServer());
            throw AuthErrorCodes.UNKNOWN_AUTHENTICATION_SERVER.asThrowable();
        }

        Result<String> result = authenticationFacade.authenticate(authenticationInfo);
        if (!result.isSuccess()) {
            log.error("authenticate error, authenticationServer: {}",
                client.getAuthenticationServer());
            throw AuthErrorCodes.AUTHENTICATE_ERROR.asThrowable();
        }

        String subjectId = result.getData();
        if (subjectId == null) {
            log.warn("authenticate failed, authenticationServer: {}",
                client.getAuthenticationServer());
            throw AuthErrorCodes.AUTHENTICATE_FAILED.asThrowable();
        }
        return subjectId;
    }

    protected void checkGrantType(GrantType grantType, ClientBO client) {
        OAuth2Mode mode = mode();
        if (!mode.validateGrantType(grantType)) {
            throw new IllegalArgumentException("responseType must be " + mode.getGrantType());
        }
        if (!client.supportedGrantType(grantType)) {
            log.warn("client unsupported grantType, clientId: {}, grantType: {}",
                client.getId(), grantType);
            throw AuthErrorCodes.CLIENT_UNSUPPORTED_GRANT_TYPE.asThrowable();
        }
    }

    protected void checkClientSecret(String clientSecret, ClientBO client) {
        if (!client.validateSecret(clientSecret)) {
            log.warn("clientSecret error, clientId: {}, clientSecret: {}",
                client.getId(), clientSecret);
            throw AuthErrorCodes.CLIENT_SECRET_ERROR.asThrowable();
        }
    }

    protected OAuth2TokenDTO generateToken(String subjectId, String scope, ClientBO client) {
        OAuth2TokenBO oauth2Token = OAuth2TokenBO.generate(
            oauth2TokenRepository.generateId(), client.getId(), subjectId, scope);
        if (!oauth2TokenRepository.add(oauth2Token)) {
            log.warn("token create error, oauth2Token: {}", GsonUtils.toJson(oauth2Token));
            throw AuthErrorCodes.TOKEN_CREATE_FAILED.asThrowable();
        }
        return oauth2Token.toDTO(client.getAccessTokenExpireSeconds());
    }

}
