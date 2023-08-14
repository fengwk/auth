package fun.fengwk.auth.share.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author fengwk
 */
@Getter
@AllArgsConstructor
public enum TokenType {

    BEARER("Bearer");

    private final String code;

    public static TokenType of(String code) {
        for (TokenType type : TokenType.values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }

}
