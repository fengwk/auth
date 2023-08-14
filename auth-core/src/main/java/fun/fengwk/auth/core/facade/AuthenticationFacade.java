package fun.fengwk.auth.core.facade;

import fun.fengwk.convention4j.api.result.Result;

import java.util.List;
import java.util.Map;

/**
 * 认证服务器
 *
 * @author fengwk
 */
public interface AuthenticationFacade {

    /**
     * 获取认证服务器标识符
     *
     * @return 认证服务器标识符
     */
    String server();

    /**
     * 认证
     *
     * @param authenticationInfo 认证信息
     * @return data：认证成功返回subjectId，否则返回null
     */
    Result<String> authenticate(Map<String, Object> authenticationInfo);

    /**
     * 获取当前支持的所有scope列表
     *
     * @return scope列表
     */
    Result<List<String>> listScopes();

    /**
     * 获取主体信息
     *
     * @param subjectId 主体
     * @param scope 授权范围
     * @return 主体信息
     */
    Map<String, Object> getSubject(String subjectId, String scope);

}