package fun.fengwk.auth.share.model;

import fun.fengwk.auth.share.constant.OAuth2Mode;
import lombok.Data;

import java.util.List;

/**
 * @author fengwk
 */
@Data
public class ClientDTO {

    private Long id;
    private String name;
    private String description;
    private String secret;
    /**
     * {@link OAuth2Mode}
     */
    private List<String> oauth2Modes;
    private List<String> redirectUris;
    private String authenticationServer;
    private Integer authorizationCodeExpireSeconds; // 授权码超时，seconds
    private Integer accessTokenExpireSeconds; // 访问令牌超时，seconds
    private Integer refreshTokenExpireSeconds; // 刷新令牌超时，seconds
    private Integer authorizationExpireSeconds; // 授权超时，seconds

}
