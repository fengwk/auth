package fun.fengwk.auth.share.model;

import lombok.Data;

/**
 * @author fengwk
 */
@Data
public class OAuth2TokenDTO {

    private String accessToken;
    private String tokenType;
    private Integer expiresIn;
    private String refreshToken;
    private String scope;

}
