package fun.fengwk.auth.core.facade;

import fun.fengwk.auth.core.model.ClientBO;
import fun.fengwk.auth.core.repo.ClientRepository;
import lombok.AllArgsConstructor;

/**
 * @author fengwk
 */
@AllArgsConstructor
public class DefaultClientFacade implements ClientFacade {

    private final ClientRepository clientRepository;

    @Override
    public ClientBO getByClientId(String clientId) {
        return clientRepository.getById(clientId);
    }

}
