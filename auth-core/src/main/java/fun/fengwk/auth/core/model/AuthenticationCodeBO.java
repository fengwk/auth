package fun.fengwk.auth.core.model;

import fun.fengwk.auth.share.constant.ResponseType;
import lombok.Data;

import java.util.UUID;

/**
 * @author fengwk
 */
@Data
public class AuthenticationCodeBO {

    private String code;
    private String subjectId;
    private ResponseType responseType;
    private String clientId;
    private String redirectUri;
    private String scope;
    private String state;

    public static AuthenticationCodeBO generate(
        String subjectId,
        ResponseType responseType,
        String clientId,
        String redirectUri,
        String scope,
        String state) {
        AuthenticationCodeBO authenticationCode = new AuthenticationCodeBO();
        authenticationCode.setCode(generateCode());
        authenticationCode.setSubjectId(subjectId);
        authenticationCode.setResponseType(responseType);
        authenticationCode.setClientId(clientId);
        authenticationCode.setRedirectUri(redirectUri);
        authenticationCode.setScope(scope);
        authenticationCode.setState(state);
        return authenticationCode;
    }

    private static String generateCode() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            sb.append(UUID.randomUUID().toString().replace("-", ""));
        }
        return sb.toString();
    }

}
