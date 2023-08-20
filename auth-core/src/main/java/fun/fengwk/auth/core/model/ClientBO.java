package fun.fengwk.auth.core.model;

import fun.fengwk.auth.share.constant.GrantType;
import fun.fengwk.auth.share.constant.OAuth2Mode;
import fun.fengwk.auth.share.constant.ResponseType;
import fun.fengwk.auth.share.model.BaseClientPropertiesDTO;
import fun.fengwk.auth.share.model.ClientDTO;
import fun.fengwk.auth.share.model.ClientSaveDTO;
import fun.fengwk.convention4j.common.NullSafe;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @author fengwk
 */
@Data
public class ClientBO {

    private Long id;
    private String name;
    private String description;
    private String secret;
    private List<OAuth2Mode> oauth2Modes;
    private List<String> redirectUris;
    private String authenticationServer;
    private Integer authorizationCodeExpireSeconds; // 授权码超时，seconds
    private Integer accessTokenExpireSeconds; // 访问令牌超时，seconds
    private Integer refreshTokenExpireSeconds; // 刷新令牌超时，seconds
    private Integer authorizationExpireSeconds; // 授权超时，seconds

    public static ClientBO create(BaseClientPropertiesDTO propertiesDTO, Long id) {
        ClientBO clientBO = new ClientBO();
        clientBO.setId(id);
        setByPropertiesDTO(clientBO, propertiesDTO);
        if (StringUtils.isEmpty(clientBO.getSecret())) {
            clientBO.setSecret(generateSecret());
        }
        check(clientBO);
        return clientBO;
    }

    private static void setByPropertiesDTO(ClientBO clientBO, BaseClientPropertiesDTO propertiesDTO) {
        clientBO.setName(propertiesDTO.getName());
        clientBO.setDescription(propertiesDTO.getDescription());
        if (StringUtils.isNotEmpty(propertiesDTO.getSecret())) {
            clientBO.setSecret(propertiesDTO.getSecret());
        }
        clientBO.setOauth2Modes(NullSafe.map(propertiesDTO.getOauth2Modes(), OAuth2Mode::of));
        clientBO.setRedirectUris(NullSafe.of(propertiesDTO.getRedirectUris()));
        clientBO.setAuthenticationServer(propertiesDTO.getAuthenticationServer());
        clientBO.setAuthorizationCodeExpireSeconds(propertiesDTO.getAuthorizationCodeExpireSeconds());
        clientBO.setAccessTokenExpireSeconds(propertiesDTO.getAccessTokenExpireSeconds());
        clientBO.setRefreshTokenExpireSeconds(propertiesDTO.getRefreshTokenExpireSeconds());
        clientBO.setAuthorizationExpireSeconds(propertiesDTO.getAuthorizationExpireSeconds());
    }

    private static void check(ClientBO clientBO) {
        if (clientBO.getId() == null) {
            throw new IllegalArgumentException("id is null");
        }
        if (StringUtils.isEmpty(clientBO.getName())) {
            throw new IllegalArgumentException("name is empty");
        }
        if (StringUtils.isEmpty(clientBO.getSecret())) {
            throw new IllegalArgumentException("secret is empty");
        }
        if (CollectionUtils.isEmpty(clientBO.getOauth2Modes())) {
            throw new IllegalArgumentException("oauth2Modes is empty");
        }
        if (CollectionUtils.isEmpty(clientBO.getRedirectUris())) {
            throw new IllegalArgumentException("redirectUris is empty");
        }
        if (clientBO.getOauth2Modes().contains(OAuth2Mode.AUTHORIZATION_CODE)) {
            if (clientBO.getAuthorizationCodeExpireSeconds() == null) {
                throw new IllegalArgumentException("authorizationCodeExpireSeconds is null");
            }
            if (clientBO.getAuthorizationCodeExpireSeconds() <= 0) {
                throw new IllegalArgumentException("authorizationCodeExpireSeconds <= 0");
            }
        }
        if (clientBO.getAccessTokenExpireSeconds() == null) {
            throw new IllegalArgumentException("accessTokenExpireSeconds is null");
        }
        if (clientBO.getRefreshTokenExpireSeconds() == null) {
            throw new IllegalArgumentException("accessTokenExpireSeconds is null");
        }
        if (clientBO.getAuthorizationExpireSeconds() == null) {
            throw new IllegalArgumentException("accessTokenExpireSeconds is null");
        }
    }

    private static String generateSecret() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            sb.append(UUID.randomUUID().toString().replace("-", ""));
        }
        return sb.toString();
    }

    public void update(ClientSaveDTO saveDTO) {
        setByPropertiesDTO(this, saveDTO);
        check(this);
    }

    public void updateSecret() {
        setSecret(generateSecret());
    }

    public boolean validateSecret(String secret) {
        return Objects.equals(this.secret, secret);
    }

    public boolean supportedRedirectUri(String redirectUri) {
        return NullSafe.of(redirectUris).contains(redirectUri);
    }

    public boolean supportedResponseType(ResponseType responseType) {
        for (OAuth2Mode mode : NullSafe.of(oauth2Modes)) {
            if (Objects.equals(mode.getResponseType(), responseType)) {
                return true;
            }
        }
        return false;
    }

    public boolean supportedGrantType(GrantType grantType) {
        for (OAuth2Mode mode : NullSafe.of(oauth2Modes)) {
            if (Objects.equals(mode.getGrantType(), grantType)) {
                return true;
            }
        }
        return false;
    }

    public ClientDTO toDTO() {
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setId(getId());
        clientDTO.setName(getName());
        clientDTO.setDescription(getDescription());
        clientDTO.setSecret(getSecret());
        clientDTO.setOauth2Modes(NullSafe.map(getOauth2Modes(), OAuth2Mode::getCode));
        clientDTO.setRedirectUris(getRedirectUris());
        clientDTO.setAuthenticationServer(getAuthenticationServer());
        clientDTO.setAuthorizationCodeExpireSeconds(getAuthorizationCodeExpireSeconds());
        clientDTO.setAccessTokenExpireSeconds(getAccessTokenExpireSeconds());
        clientDTO.setRefreshTokenExpireSeconds(getRefreshTokenExpireSeconds());
        clientDTO.setAuthorizationExpireSeconds(getAuthorizationExpireSeconds());
        return clientDTO;
    }

}
