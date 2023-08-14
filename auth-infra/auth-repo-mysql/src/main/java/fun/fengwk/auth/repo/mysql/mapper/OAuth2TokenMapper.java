package fun.fengwk.auth.repo.mysql.mapper;

import fun.fengwk.auth.repo.mysql.model.OAuth2TokenDO;
import fun.fengwk.automapper.annotation.AutoMapper;

/**
 * @author fengwk
 */
@AutoMapper(tableName = "oauth2_token")
public interface OAuth2TokenMapper {

    int insert(OAuth2TokenDO oauth2TokenDO);

    int updateById(OAuth2TokenDO oauth2TokenDO);

    OAuth2TokenDO findByAccessToken(String accessToken);

    OAuth2TokenDO findByRefreshToken(String refreshToken);

}
