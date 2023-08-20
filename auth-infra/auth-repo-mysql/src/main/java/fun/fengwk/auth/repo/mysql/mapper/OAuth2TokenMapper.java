package fun.fengwk.auth.repo.mysql.mapper;

import fun.fengwk.auth.repo.mysql.model.OAuth2TokenDO;
import fun.fengwk.automapper.annotation.AutoMapper;
import fun.fengwk.convention4j.springboot.starter.cache.annotation.CacheReadMethod;
import fun.fengwk.convention4j.springboot.starter.cache.annotation.CacheWriteMethod;
import fun.fengwk.convention4j.springboot.starter.cache.annotation.Key;
import fun.fengwk.convention4j.springboot.starter.cache.mapper.LongIdCacheMapper;

/**
 * @author fengwk
 */
@AutoMapper(tableName = "oauth2_token")
public interface OAuth2TokenMapper extends LongIdCacheMapper<OAuth2TokenDO> {

    @CacheWriteMethod
    int insert(OAuth2TokenDO oauth2TokenDO);

    @CacheWriteMethod
    int updateById(OAuth2TokenDO oauth2TokenDO);

    @CacheReadMethod
    OAuth2TokenDO findByAccessToken(@Key("accessToken") String accessToken);

    @CacheReadMethod
    OAuth2TokenDO findByRefreshToken(@Key("refreshToken") String refreshToken);

}
