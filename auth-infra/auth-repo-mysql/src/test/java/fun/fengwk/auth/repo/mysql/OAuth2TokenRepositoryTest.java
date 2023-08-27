package fun.fengwk.auth.repo.mysql;

import fun.fengwk.auth.core.model.OAuth2TokenBO;
import fun.fengwk.auth.core.repo.OAuth2TokenRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author fengwk
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AuthRepoMysqlTestApplication.class)
public class OAuth2TokenRepositoryTest {

    @Autowired
    private OAuth2TokenRepository oauth2TokenRepository;

    @Transactional
    @Test
    public void testCrud() {
        OAuth2TokenBO oauth2TokenBO = OAuth2TokenBO.generate(
            oauth2TokenRepository.generateId(), "1", "1", "userinfo");

        assert oauth2TokenRepository.add(oauth2TokenBO);
        OAuth2TokenBO found = oauth2TokenRepository.getByAccessToken(oauth2TokenBO.getAccessToken());
        assert oauth2TokenBO.equals(found);
        found = oauth2TokenRepository.getByRefreshToken(oauth2TokenBO.getRefreshToken());
        assert oauth2TokenBO.equals(found);
        oauth2TokenBO.refresh();
        assert oauth2TokenRepository.updateById(oauth2TokenBO);
        assert oauth2TokenBO.equals(oauth2TokenRepository.getByAccessToken(oauth2TokenBO.getAccessToken()));
    }

}
