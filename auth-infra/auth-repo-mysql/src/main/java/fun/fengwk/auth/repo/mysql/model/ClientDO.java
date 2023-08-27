package fun.fengwk.auth.repo.mysql.model;

import fun.fengwk.auth.core.model.ClientBO;
import fun.fengwk.auth.share.constant.OAuth2Mode;
import fun.fengwk.convention4j.common.IntBool;
import fun.fengwk.convention4j.common.NullSafe;
import fun.fengwk.convention4j.common.gson.GsonUtils;
import fun.fengwk.convention4j.common.reflect.TypeToken;
import fun.fengwk.convention4j.springboot.starter.cache.annotation.Key;
import fun.fengwk.convention4j.springboot.starter.cache.mapper.BaseCacheDO;
import lombok.Data;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.List;

/**
 * @author fengwk
 */
@Data
public class ClientDO extends BaseCacheDO<Long> {

    private Integer deleted = IntBool.FALSE;
    @Key
    private String name;
    private String description;
    private String secret;
    private String oauth2Modes;
    private String redirectUris;
    private String authenticationServer;
    private Integer authorizationCodeExpireSeconds;
    private Integer accessTokenExpireSeconds;
    private Integer refreshTokenExpireSeconds;
    private Integer authorizationExpireSeconds;

    public static ClientDO forLogicDelete(Long id) {
        ClientDO clientDO = new ClientDO();
        clientDO.setId(id);
        clientDO.setDeleted(IntBool.TRUE);
        return clientDO;
    }

    public static ClientDO bo2do(ClientBO clientBO) {
        if (clientBO == null) {
            return null;
        }
        ClientDO clientDO = new ClientDO();
        clientDO.setId(NumberUtils.toLong(clientBO.getId()));
        clientDO.setName(clientBO.getName());
        clientDO.setDescription(clientBO.getDescription());
        clientDO.setSecret(clientBO.getSecret());
        clientDO.setOauth2Modes(GsonUtils.toJson(NullSafe.map(clientBO.getOauth2Modes(), OAuth2Mode::getCode)));
        clientDO.setRedirectUris(GsonUtils.toJson(clientBO.getRedirectUris()));
        clientDO.setAuthenticationServer(clientBO.getAuthenticationServer());
        clientDO.setAuthorizationCodeExpireSeconds(clientBO.getAuthorizationCodeExpireSeconds());
        clientDO.setAccessTokenExpireSeconds(clientBO.getAccessTokenExpireSeconds());
        clientDO.setRefreshTokenExpireSeconds(clientBO.getRefreshTokenExpireSeconds());
        clientDO.setAuthorizationExpireSeconds(clientBO.getAuthorizationExpireSeconds());
        return clientDO;
    }

    public ClientBO toBO() {
        ClientBO clientBO = new ClientBO();
        clientBO.setId(String.valueOf(getId()));
        clientBO.setName(getName());
        clientBO.setDescription(getDescription());
        clientBO.setSecret(getSecret());
        List<String> oauth2Modes = GsonUtils.fromJson(getOauth2Modes(), new TypeToken<List<String>>() {}.getType());
        clientBO.setOauth2Modes(NullSafe.map(oauth2Modes, OAuth2Mode::of));
        clientBO.setRedirectUris(GsonUtils.fromJson(getRedirectUris(), new TypeToken<List<String>>() {}.getType()));
        clientBO.setAuthenticationServer(getAuthenticationServer());
        clientBO.setAuthorizationCodeExpireSeconds(getAuthorizationCodeExpireSeconds());
        clientBO.setAccessTokenExpireSeconds(getAccessTokenExpireSeconds());
        clientBO.setRefreshTokenExpireSeconds(getRefreshTokenExpireSeconds());
        clientBO.setAuthorizationExpireSeconds(getAuthorizationExpireSeconds());
        return clientBO;
    }

}
