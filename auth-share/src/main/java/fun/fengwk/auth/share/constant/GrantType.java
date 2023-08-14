package fun.fengwk.auth.share.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author fengwk
 */
@Getter
@AllArgsConstructor
public enum GrantType {

    AUTHORIZATION_CODE("authorization_code"),
    PASSWORD("password"),
    CLIENT_CREDENTIALS("client_credentials"),
    REFRESH_TOKEN("refresh_token");

    private final String code;

    public static GrantType of(String code) {
        for (GrantType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }

}
