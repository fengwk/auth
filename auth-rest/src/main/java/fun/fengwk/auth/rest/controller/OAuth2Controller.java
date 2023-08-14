package fun.fengwk.auth.rest.controller;

import fun.fengwk.auth.core.service.OAuth2Service;
import fun.fengwk.auth.share.model.OAuth2TokenDTO;
import fun.fengwk.auth.share.openfeign.OAuth2FeignClient;
import fun.fengwk.convention4j.api.result.Result;
import fun.fengwk.convention4j.common.result.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Map;

/**
 * @author fengwk
 * @see <a href="https://www.ruanyifeng.com/blog/2014/05/oauth_2_0.html">The OAuth 2.0 Authorization Framework</a>
 */
@RestController
public class OAuth2Controller implements OAuth2FeignClient {

    @Autowired
    private OAuth2Service oauth2Service;

    @Override
    public Result<String> authorize(
            String responseType,
            String clientId,
            String redirectUri,
            String scope,
            String state,
            Map<String, Object> authenticationInfo) {
        URI uri = oauth2Service.authorize(
            responseType, clientId, redirectUri, scope, state, authenticationInfo);
        return Results.ok(uri.toASCIIString());
    }

    @Override
    public Result<OAuth2TokenDTO> token(
        String grantType,
        String code,
        String redirectUri,
        String clientId,
        String clientSecret,
        String scope,
        String subjectId,
        String refreshToken,
        Map<String, Object> authenticationInfo) {
        OAuth2TokenDTO res = oauth2Service.token(
            grantType, code, redirectUri, clientId, clientSecret, scope, subjectId, refreshToken, authenticationInfo);
        return Results.created(res);
    }

    @Override
    public Result<Map<String, Object>> subject(String accessToken) {
        Map<String, Object> subject = oauth2Service.subject(accessToken);
        return Results.ok(subject);
    }

}
