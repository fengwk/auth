package fun.fengwk.auth.repo.mysql;

import fun.fengwk.auth.core.model.OAuth2TokenBO;
import fun.fengwk.auth.core.repo.OAuth2TokenRepository;
import fun.fengwk.auth.repo.mysql.mapper.OAuth2TokenMapper;
import fun.fengwk.auth.repo.mysql.model.OAuth2TokenDO;
import fun.fengwk.convention4j.common.NullSafe;
import fun.fengwk.convention4j.common.StringUtils;
import fun.fengwk.convention4j.common.idgen.NamespaceIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author fengwk
 */
@Repository
public class MysqlOAuth2TokenRepository implements OAuth2TokenRepository {

    @Autowired
    private NamespaceIdGenerator<Long> idGenerator;
    @Autowired
    private OAuth2TokenMapper oauth2TokenMapper;

    @Override
    public Long generateId() {
        return idGenerator.next(getClass());
    }

    @Override
    public boolean add(OAuth2TokenBO oauth2TokenBO) {
        if (oauth2TokenBO == null) {
            return false;
        }
        OAuth2TokenDO oauth2TokenDO = OAuth2TokenDO.bo2do(oauth2TokenBO);
        return oauth2TokenMapper.insert(oauth2TokenDO) > 0;
    }

    @Override
    public boolean updateById(OAuth2TokenBO oauth2TokenBO) {
        if (oauth2TokenBO == null) {
            return false;
        }
        OAuth2TokenDO oauth2TokenDO = OAuth2TokenDO.bo2do(oauth2TokenBO);
        return oauth2TokenMapper.updateById(oauth2TokenDO) > 0;
    }

    @Override
    public OAuth2TokenBO getByAccessToken(String accessToken) {
        if (StringUtils.isEmpty(accessToken)) {
            return null;
        }
        OAuth2TokenDO oauth2TokenDO = oauth2TokenMapper.findByAccessToken(accessToken);
        return NullSafe.map(oauth2TokenDO, OAuth2TokenDO::toBO);
    }

    @Override
    public OAuth2TokenBO getByRefreshToken(String refreshToken) {
        if (StringUtils.isEmpty(refreshToken)) {
            return null;
        }
        OAuth2TokenDO oauth2TokenDO = oauth2TokenMapper.findByRefreshToken(refreshToken);
        return NullSafe.map(oauth2TokenDO, OAuth2TokenDO::toBO);
    }

}
