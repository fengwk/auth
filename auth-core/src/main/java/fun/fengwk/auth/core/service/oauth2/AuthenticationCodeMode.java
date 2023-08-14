package fun.fengwk.auth.core.service.oauth2;

import fun.fengwk.auth.core.cache.AuthenticationCodeCache;
import fun.fengwk.auth.core.model.AuthenticationCodeBO;
import fun.fengwk.auth.core.model.ClientBO;
import fun.fengwk.auth.share.constant.AuthErrorCodes;
import fun.fengwk.auth.share.constant.GrantType;
import fun.fengwk.auth.share.constant.OAuth2Mode;
import fun.fengwk.auth.share.constant.ResponseType;
import fun.fengwk.auth.share.model.OAuth2TokenDTO;
import fun.fengwk.convention4j.common.NullSafe;
import fun.fengwk.convention4j.common.gson.GsonUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.Map;
import java.util.Objects;

/**
 * 授权码模式
 *
 * @author fengwk
 */
@Slf4j
@Validated
@Component
public class AuthenticationCodeMode extends BaseOAuth2Mode {

    @Autowired
    private AuthenticationCodeCache authenticationCodeCache;

    @Override
    protected OAuth2Mode mode() {
        return OAuth2Mode.AUTHORIZATION_CODE;
    }

    /**
     *
     * @param responseType 表示授权类型，必选项，此处的值固定为"code"
     * @param client 客户端
     * @param redirectUri 表示重定向URI
     * @param scope 表示申请的权限范围
     * @param state 表示客户端的当前状态，可以指定任意值，认证服务器会原封不动地返回这个值
     * @param authenticationInfo 认证信息
     * @return authorizationCode
     */
    public URI authorize(
        @NotNull ResponseType responseType,
        @NotNull ClientBO client,
        @NotEmpty String redirectUri,
        String scope,
        String state,
        @NotNull Map<String, Object> authenticationInfo) {
        checkResponseType(responseType, client);
        HttpUrl rUri = checkRedirectUri(redirectUri, client);
        checkScope(scope, client);
        String subjectId = authenticate(authenticationInfo, client);
        String code = generateAuthenticationCode(
            responseType, client, redirectUri, scope, state, subjectId);
        return buildAuthorizeURI(rUri, code, state);
    }

    /**
     * token - 授权码模式
     *
     * @param grantType 表示使用的授权模式，此处的值固定为"authorization_code"
     * @param code 表示上一步获得的授权码
     * @param redirectUri 表示重定向URI，必须与A步骤中的该参数值保持一致
     * @param client 客户端
     * @param clientSecret 客户端密钥
     * @return OAuth2TokenDTO
     */
    public OAuth2TokenDTO token(
        @NotNull GrantType grantType,
        @NotEmpty String code,
        @NotEmpty String redirectUri,
        @NotNull ClientBO client,
        @NotEmpty String clientSecret) {
        checkGrantType(grantType, client);
        checkClientSecret(clientSecret, client);
        AuthenticationCodeBO authenticationCode = getAndCheckAuthenticationCode(code);
        checkRedirectUri(redirectUri, authenticationCode);
        return generateToken(authenticationCode.getSubjectId(), authenticationCode.getScope(), client);
    }

    private String generateAuthenticationCode(
            ResponseType responseType, ClientBO client, String redirectUri, String scope, String state, String subjectId) {
        // 生成授权码
        AuthenticationCodeBO authenticationCode = AuthenticationCodeBO.generate(
            subjectId,
            responseType,
            client.getId(),
            redirectUri,
            scope,
            state);
        authenticationCodeCache.set(
                authenticationCode, client.getAuthorizationCodeExpireSeconds());

        // 返回授权码
        return authenticationCode.getCode();
    }

    private URI buildAuthorizeURI(HttpUrl redirectUri, String code, String state) {
        return redirectUri.newBuilder()
            .addQueryParameter("code", NullSafe.of(code, ""))
            .addQueryParameter("state", NullSafe.of(state, ""))
            .build()
            .uri();
    }

    private AuthenticationCodeBO getAndCheckAuthenticationCode(String code) {
        AuthenticationCodeBO authenticationCode = authenticationCodeCache.get(code);
        if (authenticationCode == null) {
            log.warn("authentication code error, code: {}", code);
            throw AuthErrorCodes.AUTHENTICATION_CODE_ERROR.asThrowable();
        }
        return authenticationCode;
    }

    private void checkRedirectUri(String redirectUri, AuthenticationCodeBO authenticationCode) {
        if (!Objects.equals(redirectUri, authenticationCode.getRedirectUri())) {
            log.warn("redirectUri error, redirectUri: {}, authenticationCode: {}",
                redirectUri, GsonUtils.toJson(authenticationCode));
            throw AuthErrorCodes.REDIRECT_URI_ERROR.asThrowable();
        }
    }

}
