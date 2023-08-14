package fun.fengwk.auth.core.service;

import fun.fengwk.auth.core.AuthCoreApplication;
import fun.fengwk.auth.share.constant.ClientPageSort;
import fun.fengwk.auth.share.constant.OAuth2Mode;
import fun.fengwk.auth.share.model.ClientCreateDTO;
import fun.fengwk.auth.share.model.ClientDTO;
import fun.fengwk.auth.share.model.ClientSaveDTO;
import fun.fengwk.convention4j.api.page.SortablePageQuery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author fengwk
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AuthCoreApplication.class)
public class ClientServiceTest {

    @Autowired
    private ClientService clientService;

    @Test
    public void test() {
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
        ClientDTO clientDTO = clientService.createClient(createDTO);
        assert Objects.equals(clientDTO.getName(), createDTO.getName());
        assert Objects.equals(clientDTO.getDescription(), createDTO.getDescription());
        assert Objects.equals(clientDTO.getOauth2Modes(), createDTO.getOauth2Modes());
        assert Objects.equals(clientDTO.getRedirectUris(), createDTO.getRedirectUris());
        assert Objects.equals(clientDTO.getAuthenticationServer(), createDTO.getAuthenticationServer());
        assert Objects.equals(clientDTO.getAuthorizationCodeExpireSeconds(), createDTO.getAuthorizationCodeExpireSeconds());
        assert Objects.equals(clientDTO.getAccessTokenExpireSeconds(), createDTO.getAccessTokenExpireSeconds());
        assert Objects.equals(clientDTO.getRefreshTokenExpireSeconds(), createDTO.getRefreshTokenExpireSeconds());
        assert Objects.equals(clientDTO.getAuthorizationExpireSeconds(), createDTO.getAuthorizationExpireSeconds());

        ClientSaveDTO saveDTO = new ClientSaveDTO();
        saveDTO.setId(clientDTO.getId());
        saveDTO.setName("test_client");
        saveDTO.setDescription("This is test client");
        saveDTO.setOauth2Modes(Collections.singletonList(OAuth2Mode.AUTHORIZATION_CODE.getCode()));
        saveDTO.setRedirectUris(Collections.singletonList("https://fengwk.fun/homepage"));
        saveDTO.setAuthenticationServer("default_server");
        saveDTO.setAuthorizationCodeExpireSeconds((int) TimeUnit.SECONDS.convert(1, TimeUnit.MINUTES));
        saveDTO.setAccessTokenExpireSeconds((int) TimeUnit.SECONDS.convert(30, TimeUnit.MINUTES));
        saveDTO.setRefreshTokenExpireSeconds((int) TimeUnit.SECONDS.convert(15, TimeUnit.DAYS));
        saveDTO.setAuthorizationExpireSeconds((int) TimeUnit.SECONDS.convert(30, TimeUnit.DAYS));
        ClientDTO savedDTO = clientService.saveClient(saveDTO);
        assert Objects.equals(clientDTO, savedDTO);

        ClientDTO found = clientService.get(clientDTO.getId());
        assert Objects.equals(clientDTO, found);

        SortablePageQuery sortablePageQuery = SortablePageQuery.buildWithPermittedKeyEnum(
                1, 1, "id+", ClientPageSort.class);
        assert clientService.page(sortablePageQuery).getResults().stream()
                .filter(c -> Objects.equals(c, clientDTO)).count() == 1;

        assert clientService.remove(clientDTO.getId());
        assert clientService.get(clientDTO.getId()) == null;
    }

}
