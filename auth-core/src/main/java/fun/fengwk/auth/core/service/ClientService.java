package fun.fengwk.auth.core.service;

import fun.fengwk.auth.core.model.ClientBO;
import fun.fengwk.auth.core.repo.ClientRepository;
import fun.fengwk.auth.share.constant.AuthErrorCodes;
import fun.fengwk.auth.share.model.ClientCreateDTO;
import fun.fengwk.auth.share.model.ClientDTO;
import fun.fengwk.auth.share.model.ClientSaveDTO;
import fun.fengwk.convention4j.api.page.Page;
import fun.fengwk.convention4j.api.page.SortablePageQuery;
import fun.fengwk.convention4j.common.NullSafe;
import fun.fengwk.convention4j.common.gson.GsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * @author fengwk
 */
@Slf4j
@Validated
@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public ClientDTO createClient(@NotNull ClientCreateDTO createDTO) {
        if (clientRepository.existsByName(createDTO.getName())) {
            log.warn("client already exists, name: {}", createDTO.getName());
            throw AuthErrorCodes.CLIENT_ALREADY_EXISTS.asThrowable();
        }

        ClientBO clientBO = ClientBO.create(createDTO, clientRepository.generateId());
        if (!clientRepository.add(clientBO)) {
            log.error("client create failed, client: {}", GsonUtils.toJson(clientBO));
            throw AuthErrorCodes.CLIENT_CREATE_FAILED.asThrowable();
        }

        return clientBO.toDTO();
    }

    public ClientDTO saveClient(@NotNull ClientSaveDTO saveDTO) {
        ClientBO clientBO = clientRepository.getById(saveDTO.getId());

        if (clientBO == null) {
            if (clientRepository.existsByName(saveDTO.getName())) {
                log.warn("client already exists, name: {}", saveDTO.getName());
                throw AuthErrorCodes.CLIENT_ALREADY_EXISTS.asThrowable();
            }

            clientBO = ClientBO.create(saveDTO, saveDTO.getId());
            if (!clientRepository.add(clientBO)) {
                log.error("client save failed, client: {}", GsonUtils.toJson(clientBO));
                throw AuthErrorCodes.CLIENT_SAVE_FAILED.asThrowable();
            }
        } else {
            if (!Objects.equals(saveDTO.getName(), clientBO.getName())) {
                if (clientRepository.existsByName(saveDTO.getName())) {
                    log.warn("client already exists, name: {}", saveDTO.getName());
                    throw AuthErrorCodes.CLIENT_ALREADY_EXISTS.asThrowable();
                }
            }

            clientBO.update(saveDTO);
            if (!clientRepository.updateById(clientBO)) {
                log.error("client save failed, client: {}", GsonUtils.toJson(clientBO));
                throw AuthErrorCodes.CLIENT_SAVE_FAILED.asThrowable();
            }
        }

        return clientBO.toDTO();
    }

    public boolean remove(@NotNull String clientId) {
        return clientRepository.removeById(clientId);
    }

    public ClientDTO updateSecret(@NotNull String clientId) {
        ClientBO clientBO = clientRepository.getById(clientId);
        if (clientBO == null) {
            log.warn("client not found, clientId: {}", clientId);
            throw AuthErrorCodes.CLIENT_NOT_FOUND.asThrowable();
        }

        clientBO.updateSecret();
        if (!clientRepository.add(clientBO)) {
            log.error("client update secret failed, client: {}", GsonUtils.toJson(clientBO));
            throw AuthErrorCodes.CLIENT_UPDATE_SECRET_FAILED.asThrowable();
        }

        return clientBO.toDTO();
    }

    public ClientDTO get(String clientId) {
        ClientBO clientBO = clientRepository.getById(clientId);
        return NullSafe.map(clientBO, ClientBO::toDTO);
    }

    public Page<ClientDTO> page(SortablePageQuery sortablePageQuery) {
        Page<ClientBO> clientBOPage = clientRepository.page(sortablePageQuery);
        return clientBOPage.map(ClientBO::toDTO);
    }

}
