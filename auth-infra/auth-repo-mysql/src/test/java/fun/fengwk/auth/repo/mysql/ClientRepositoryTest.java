package fun.fengwk.auth.repo.mysql;

import fun.fengwk.auth.core.model.ClientBO;
import fun.fengwk.auth.core.repo.ClientRepository;
import fun.fengwk.auth.share.constant.ClientPageSort;
import fun.fengwk.auth.share.constant.OAuth2Mode;
import fun.fengwk.auth.share.model.ClientCreateDTO;
import fun.fengwk.auth.share.model.ClientSaveDTO;
import fun.fengwk.convention4j.api.page.Page;
import fun.fengwk.convention4j.api.page.SortablePageQuery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author fengwk
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AuthRepoMysqlTestApplication.class)
public class ClientRepositoryTest {

    @Autowired
    private ClientRepository clientRepository;

    @Test
    public void testGenerateId() {
        int genCount = 100;
        Set<String> set = new HashSet<>();
        for (int i = 0; i < genCount; i++) {
            set.add(clientRepository.generateId());
        }
        assert set.size() == genCount;
    }

    @Transactional
    @Test
    public void testCrud() {
        ClientCreateDTO createDTO = new ClientCreateDTO();
        createDTO.setName("test_client");
        createDTO.setDescription("This is test client");
        createDTO.setOauth2Modes(Collections.singletonList(OAuth2Mode.AUTHORIZATION_CODE.getCode()));
        createDTO.setRedirectUris(Collections.singletonList("https://fengwk.fun/homepage"));
        createDTO.setAuthenticationServer("default_server");
        createDTO.setAuthorizationCodeExpireSeconds((int) TimeUnit.SECONDS.convert(1, TimeUnit.MINUTES));
        createDTO.setAccessTokenExpireSeconds((int) TimeUnit.SECONDS.convert(30, TimeUnit.MINUTES));
        createDTO.setRefreshTokenExpireSeconds((int) TimeUnit.SECONDS.convert(15, TimeUnit.DAYS));
        createDTO.setAuthorizationExpireSeconds((int) TimeUnit.SECONDS.convert(30, TimeUnit.DAYS));
        ClientBO clientBO = ClientBO.create(createDTO, clientRepository.generateId());
        assert clientRepository.add(clientBO);

        ClientBO found = clientRepository.getById(clientBO.getId());
        assert clientBO.equals(found);

        ClientSaveDTO saveDTO = new ClientSaveDTO();
        BeanUtils.copyProperties(createDTO, saveDTO);
        saveDTO.setId(clientBO.getId());
        saveDTO.setName("test_client_update");
        saveDTO.setName("This is test client update");
        saveDTO.setOauth2Modes(Arrays.asList(OAuth2Mode.AUTHORIZATION_CODE.getCode(), OAuth2Mode.CLIENT_CREDENTIALS.getCode()));
        saveDTO.setRedirectUris(Collections.singletonList("https://fengwk.fun/homepage/update"));
        saveDTO.setAuthenticationServer("default_server_update");
        saveDTO.setAuthorizationCodeExpireSeconds((int) TimeUnit.SECONDS.convert(2, TimeUnit.MINUTES));
        saveDTO.setAccessTokenExpireSeconds((int) TimeUnit.SECONDS.convert(60, TimeUnit.MINUTES));
        saveDTO.setRefreshTokenExpireSeconds((int) TimeUnit.SECONDS.convert(30, TimeUnit.DAYS));
        saveDTO.setAuthorizationExpireSeconds((int) TimeUnit.SECONDS.convert(60, TimeUnit.DAYS));
        clientBO.update(saveDTO);
        assert clientRepository.updateById(clientBO);

        ClientBO foundUpdated = clientRepository.getById(clientBO.getId());
        assert clientBO.equals(foundUpdated);

        SortablePageQuery sortablePageQuery = SortablePageQuery.buildWithPermittedKeyEnum(
                1, 1, "id-", ClientPageSort.class);
        Page<ClientBO> page = clientRepository.page(sortablePageQuery);
        assert !page.getResults().isEmpty();

        assert clientRepository.removeById(clientBO.getId());
        assert clientRepository.getById(clientBO.getId()) == null;
    }

}
