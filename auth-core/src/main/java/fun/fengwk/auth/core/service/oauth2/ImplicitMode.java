package fun.fengwk.auth.core.service.oauth2;

import fun.fengwk.auth.core.model.ClientBO;
import fun.fengwk.auth.share.constant.OAuth2Mode;
import fun.fengwk.auth.share.constant.ResponseType;
import fun.fengwk.auth.share.model.OAuth2TokenDTO;
import fun.fengwk.convention4j.common.NullSafe;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.Map;
import java.util.Optional;

/**
 * 隐式模式
 *
 * @author fengwk
 */
@Slf4j
@Validated
@Component
public class ImplicitMode extends BaseOAuth2Mode {

    @Override
    protected OAuth2Mode mode() {
        return OAuth2Mode.IMPLICIT;
    }

    /**
     *
     * @param responseType 表示授权类型，必选项，此处的值固定为"code"
     * @param client 客户端，必选项
     * @param redirectUri 表示重定向URI，必选项
     * @param scope 表示申请的权限范围，可选项
     * @param state 表示客户端的当前状态，可以指定任意值，认证服务器会原封不动地返回这个值
     * @param authenticationInfo 认证信息
     * @return accessToken
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
        OAuth2TokenDTO oauth2TokenDTO = generateToken(subjectId, scope, client);
        return buildAuthorizeURI(rUri, oauth2TokenDTO, state);
    }

    private URI buildAuthorizeURI(HttpUrl redirectUri, OAuth2TokenDTO oauth2TokenDTO, String state) {
        return redirectUri.newBuilder()
            .addQueryParameter("accessToken",
                NullSafe.of(oauth2TokenDTO.getAccessToken(), ""))
            .addQueryParameter("tokenType",
                NullSafe.of(oauth2TokenDTO.getTokenType(), ""))
            .addQueryParameter("expiresIn",
                Optional.ofNullable(oauth2TokenDTO.getExpiresIn())
                    .map(String::valueOf)
                    .orElse(""))
            .addQueryParameter("state",
                NullSafe.of(state, ""))
            .build()
            .uri();
    }

}
