package fun.fengwk.auth.share.openfeign;

import fun.fengwk.auth.share.constant.ResponseType;
import fun.fengwk.auth.share.model.OAuth2TokenDTO;
import fun.fengwk.convention4j.api.result.Result;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author fengwk
 */
@RequestMapping("/oauth2")
public interface OAuth2FeignClient{

    /**
     * oauth2授权请求
     *
     * @param responseType 授权类型，{@link ResponseType}
     * @param clientId 客户端id
     * @param redirectUri 授权完成重定向地址
     * @param scope 授权范围
     * @param state 状态信息，原样返回
     * @param authenticationInfo 认证信息
     * @return redirectUri，并且携带相应信息
     */
    @GetMapping("/authorize")
    Result<String> authorize(
            @RequestParam("responseType") String responseType,
            @RequestParam("clientId") String clientId,
            @RequestParam(value = "redirectUri") String redirectUri,
            @RequestParam(value = "scope", required = false) String scope,
            @RequestParam(value = "state", required = false) String state,
            @RequestBody Map<String, Object> authenticationInfo);

    @PostMapping("/token")
    Result<OAuth2TokenDTO> token(
        @RequestParam("grantType") String grantType,
        @RequestParam(value = "code", required = false) String code,
        @RequestParam(value = "redirectUri", required = false) String redirectUri,
        @RequestParam(value = "clientId", required = false) String clientId,
        @RequestParam(value = "clientSecret", required = false) String clientSecret,
        @RequestParam(value = "scope", required = false) String scope,
        @RequestParam(value = "subjectId", required = false) String subjectId,
        @RequestParam(value = "refreshToken", required = false) String refreshToken,
        @RequestBody(required = false) Map<String, Object> authenticationInfo);

    @GetMapping("/subject")
    Result<Map<String, Object>> subject(
        @RequestParam("accessToken") String accessToken);

}
