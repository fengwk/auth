package fun.fengwk.auth.core.service.oauth2;

import fun.fengwk.auth.core.model.ClientBO;
import fun.fengwk.auth.share.constant.GrantType;
import fun.fengwk.auth.share.constant.OAuth2Mode;
import fun.fengwk.auth.share.model.OAuth2TokenDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author fengwk
 */
@Slf4j
@Validated
@Component
public class ClientCredentialsMode extends BaseOAuth2Mode {

    @Override
    protected OAuth2Mode mode() {
        return OAuth2Mode.CLIENT_CREDENTIALS;
    }

    public OAuth2TokenDTO token(
        @NotNull GrantType grantType,
        @NotNull ClientBO client,
        @NotEmpty String clientSecret,
        String scope,
        @NotEmpty String subjectId) {
        checkGrantType(grantType, client);
        checkClientSecret(clientSecret, client);
        checkScope(scope, client);
        return generateToken(subjectId, scope, client);
    }

}
