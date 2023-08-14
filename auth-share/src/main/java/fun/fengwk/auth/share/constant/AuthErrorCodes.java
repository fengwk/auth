package fun.fengwk.auth.share.constant;

import fun.fengwk.convention4j.api.code.ConventionErrorCode;
import fun.fengwk.convention4j.api.code.HttpStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author fengwk
 */
@Getter
@AllArgsConstructor
public enum AuthErrorCodes implements ConventionErrorCode {

    INVALID_CLIENT_ID(1000, HttpStatus.BAD_REQUEST),
    CLIENT_NOT_FOUND(1001, HttpStatus.BAD_REQUEST),
    CLIENT_UNSUPPORTED_RESPONSE_TYPE(1002, HttpStatus.BAD_REQUEST),
    CLIENT_UNSUPPORTED_GRANT_TYPE(1003, HttpStatus.BAD_REQUEST),
    CLIENT_UNSUPPORTED_REDIRECT_URI(1004, HttpStatus.BAD_REQUEST),
    UNKNOWN_RESPONSE_TYPE(1005, HttpStatus.BAD_REQUEST),
    UNKNOWN_GRANT_TYPE(1006, HttpStatus.BAD_REQUEST),
    UNKNOWN_AUTHENTICATION_TYPE(1007, HttpStatus.BAD_REQUEST),
    UNKNOWN_AUTHENTICATION_SERVER(1008, HttpStatus.INTERNAL_SERVER_ERROR),
    AUTHENTICATE_ERROR(1009, HttpStatus.INTERNAL_SERVER_ERROR),
    AUTHENTICATE_FAILED(1010, HttpStatus.BAD_REQUEST),
    AUTHENTICATION_CODE_ERROR(1011, HttpStatus.BAD_REQUEST),
    REDIRECT_URI_ERROR(1012, HttpStatus.BAD_REQUEST),
    CLIENT_SECRET_ERROR(1013, HttpStatus.BAD_REQUEST),
    AUTHENTICATION_UNSUPPORTED_SCOPE(1014, HttpStatus.BAD_REQUEST),
    ACCESS_TOKEN_EXPIRED(1015, HttpStatus.BAD_REQUEST),
    REFRESH_TOKEN_EXPIRED(1016, HttpStatus.BAD_REQUEST),
    AUTHORIZATION_EXPIRED(1017, HttpStatus.BAD_REQUEST),
    ACCESS_TOKEN_ERROR(1018, HttpStatus.BAD_REQUEST),
    REFRESH_TOKEN_ERROR(1019, HttpStatus.BAD_REQUEST),
    TOKEN_REFRESH_FAILED(1020, HttpStatus.INTERNAL_SERVER_ERROR),
    TOKEN_CREATE_FAILED(1021, HttpStatus.INTERNAL_SERVER_ERROR),
    CLIENT_CREATE_FAILED(1022, HttpStatus.INTERNAL_SERVER_ERROR),
    CLIENT_SAVE_FAILED(1023, HttpStatus.INTERNAL_SERVER_ERROR),
    CLIENT_UPDATE_SECRET_FAILED(1024, HttpStatus.INTERNAL_SERVER_ERROR),
    CLIENT_ALREADY_EXISTS(1025, HttpStatus.BAD_REQUEST),
    ;

    private final int domainCode;
    private final HttpStatus httpStatus;

    @Override
    public String getDomain() {
        return "AUTH";
    }

    @Override
    public String getMessage() {
        return name();
    }

}
