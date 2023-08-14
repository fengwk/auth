package fun.fengwk.auth.share.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * @author fengwk
 */
@Getter
@AllArgsConstructor
public enum OAuth2Mode {

    AUTHORIZATION_CODE("authorization_code", ResponseType.CODE, GrantType.AUTHORIZATION_CODE),
    IMPLICIT("implicit", ResponseType.TOKEN, null),
    PASSWORD("password", null, GrantType.PASSWORD),
    CLIENT_CREDENTIALS("client_credentials", null, GrantType.CLIENT_CREDENTIALS);

    private final String code;
    private final ResponseType responseType;
    private final GrantType grantType;

    public static OAuth2Mode of(String code) {
        for (OAuth2Mode mode : values()) {
            if (mode.code.equals(code)) {
                return mode;
            }
        }
        return null;
    }

    public boolean validateResponseType(ResponseType responseType) {
        return this.responseType == null || Objects.equals(this.responseType, responseType);
    }

    public boolean validateGrantType(GrantType grantType) {
        return this.grantType == null || Objects.equals(this.grantType, grantType);
    }

}
