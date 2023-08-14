package fun.fengwk.auth.core.service;

import fun.fengwk.auth.core.AuthCoreApplication;
import fun.fengwk.auth.core.facade.TestAuthenticationFacade;
import fun.fengwk.auth.share.constant.GrantType;
import fun.fengwk.auth.share.constant.OAuth2Mode;
import fun.fengwk.auth.share.constant.ResponseType;
import fun.fengwk.auth.share.constant.TokenType;
import fun.fengwk.auth.share.model.ClientCreateDTO;
import fun.fengwk.auth.share.model.ClientDTO;
import fun.fengwk.auth.share.model.OAuth2TokenDTO;
import okhttp3.HttpUrl;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author fengwk
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AuthCoreApplication.class)
public class OAuth2ServiceTest {

    @Autowired
    private ClientService clientService;
    @Autowired
    private OAuth2Service oauth2Service;
    private String redirectUri = "https://fengwk.fun/homepage";
    private Long clientId;
    private String clientSecret;

    @Before
    public void initClient() {
        ClientCreateDTO createDTO = new ClientCreateDTO();
        createDTO.setName("test_client");
        createDTO.setDescription("This is test client");
        createDTO.setOauth2Modes(Arrays.asList(
                OAuth2Mode.AUTHORIZATION_CODE.getCode(),
                OAuth2Mode.IMPLICIT.getCode(),
                OAuth2Mode.PASSWORD.getCode(),
                OAuth2Mode.CLIENT_CREDENTIALS.getCode()
        ));
        createDTO.setRedirectUris(Collections.singletonList(redirectUri));
        createDTO.setAuthenticationServer(TestAuthenticationFacade.SERVER);
        createDTO.setAuthorizationCodeExpireSeconds((int) TimeUnit.SECONDS.convert(1, TimeUnit.MINUTES));
        createDTO.setAccessTokenExpireSeconds((int) TimeUnit.SECONDS.convert(30, TimeUnit.MINUTES));
        createDTO.setRefreshTokenExpireSeconds((int) TimeUnit.SECONDS.convert(15, TimeUnit.DAYS));
        createDTO.setAuthorizationExpireSeconds((int) TimeUnit.SECONDS.convert(30, TimeUnit.DAYS));
        ClientDTO clientDTO = clientService.createClient(createDTO);
        clientId = clientDTO.getId();
        clientSecret = clientDTO.getSecret();
    }

    @After
    public void destroyClient() {
        clientService.remove(clientId);
    }

    @Test
    public void testAuthenticationMode() {
        String state = "123";
        Map<String, Object> authenticationInfo = new HashMap<>();
        authenticationInfo.put("username", "fengwk");
        URI uri = oauth2Service.authorize(
                ResponseType.CODE.getCode(), String.valueOf(clientId), redirectUri, "userInfo", state, authenticationInfo);
        HttpUrl httpUrl = HttpUrl.get(uri);
        assert Objects.equals(httpUrl.queryParameter("state"), state);
        String code = httpUrl.queryParameter("code");

        OAuth2TokenDTO oauth2TokenDTO = oauth2Service.token(
                GrantType.AUTHORIZATION_CODE.getCode(), code, redirectUri, String.valueOf(clientId), clientSecret,
                "userInfo", null, null, null);
        assert oauth2TokenDTO != null;
        assert StringUtils.isNotEmpty(oauth2TokenDTO.getAccessToken());
        assert StringUtils.isNotEmpty(oauth2TokenDTO.getRefreshToken());
        assert StringUtils.isNotEmpty(oauth2TokenDTO.getScope());
        assert StringUtils.isNotEmpty(oauth2TokenDTO.getTokenType());
        assert oauth2TokenDTO.getExpiresIn() > 0;

        OAuth2TokenDTO refreshedDTO = oauth2Service.token(
                GrantType.REFRESH_TOKEN.getCode(), null, null, String.valueOf(clientId), clientSecret,
                null, null, oauth2TokenDTO.getRefreshToken(), null);
        assert refreshedDTO != null;
        assert StringUtils.isNotEmpty(refreshedDTO.getAccessToken());
        assert StringUtils.isNotEmpty(refreshedDTO.getRefreshToken());
        assert StringUtils.isNotEmpty(refreshedDTO.getScope());
        assert StringUtils.isNotEmpty(refreshedDTO.getTokenType());
        assert refreshedDTO.getExpiresIn() > 0;
        assert !Objects.equals(refreshedDTO.getAccessToken(), oauth2TokenDTO.getAccessToken());
        assert !Objects.equals(refreshedDTO.getRefreshToken(), oauth2TokenDTO.getRefreshToken());
    }

    @Test
    public void testImplicitMode() {
        String state = "123";
        Map<String, Object> authenticationInfo = new HashMap<>();
        authenticationInfo.put("username", "fengwk");
        URI uri = oauth2Service.authorize(
                ResponseType.TOKEN.getCode(), String.valueOf(clientId), redirectUri, "userInfo", state, authenticationInfo);
        HttpUrl httpUrl = HttpUrl.get(uri);
        System.out.println(httpUrl);
        assert Objects.equals(httpUrl.queryParameter("state"), state);
        assert NumberUtils.toInt(httpUrl.queryParameter("expiresIn")) > 0;
        assert Objects.equals(httpUrl.queryParameter("tokenType"), TokenType.BEARER.getCode());
        assert StringUtils.isNotEmpty(httpUrl.queryParameter("accessToken"));
    }

    @Test
    public void testPasswordMode() {
        Map<String, Object> authenticationInfo = new HashMap<>();
        authenticationInfo.put("username", "fengwk");
        OAuth2TokenDTO oauth2TokenDTO = oauth2Service.token(
                GrantType.PASSWORD.getCode(), null, null, String.valueOf(clientId), clientSecret,
                "userInfo", null, null, authenticationInfo);
        assert oauth2TokenDTO != null;
        assert StringUtils.isNotEmpty(oauth2TokenDTO.getAccessToken());
        assert StringUtils.isNotEmpty(oauth2TokenDTO.getRefreshToken());
        assert StringUtils.isNotEmpty(oauth2TokenDTO.getScope());
        assert StringUtils.isNotEmpty(oauth2TokenDTO.getTokenType());
        assert oauth2TokenDTO.getExpiresIn() > 0;
    }

    @Test
    public void testClientCredentialsMode() {
        OAuth2TokenDTO oauth2TokenDTO = oauth2Service.token(
                GrantType.CLIENT_CREDENTIALS.getCode(), null, null, String.valueOf(clientId), clientSecret,
                "userInfo", "1", null, null);
        assert oauth2TokenDTO != null;
        assert StringUtils.isNotEmpty(oauth2TokenDTO.getAccessToken());
        assert StringUtils.isNotEmpty(oauth2TokenDTO.getRefreshToken());
        assert StringUtils.isNotEmpty(oauth2TokenDTO.getScope());
        assert StringUtils.isNotEmpty(oauth2TokenDTO.getTokenType());
        assert oauth2TokenDTO.getExpiresIn() > 0;
    }

}
