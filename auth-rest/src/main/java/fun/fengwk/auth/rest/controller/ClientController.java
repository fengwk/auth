package fun.fengwk.auth.rest.controller;

import fun.fengwk.auth.core.service.ClientService;
import fun.fengwk.auth.share.constant.ClientPageSort;
import fun.fengwk.auth.share.model.ClientCreateDTO;
import fun.fengwk.auth.share.model.ClientDTO;
import fun.fengwk.auth.share.model.ClientSaveDTO;
import fun.fengwk.auth.share.openfeign.ClientFeignClient;
import fun.fengwk.convention4j.api.page.Page;
import fun.fengwk.convention4j.api.page.SortablePageQuery;
import fun.fengwk.convention4j.api.result.Result;
import fun.fengwk.convention4j.common.result.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fengwk
 */
@RestController
public class ClientController implements ClientFeignClient {

    @Autowired
    private ClientService clientService;

    @Override
    public Result<ClientDTO> create(ClientCreateDTO createDTO) {
        ClientDTO clientDTO = clientService.createClient(createDTO);
        return Results.created(clientDTO);
    }

    @Override
    public Result<ClientDTO> update(ClientSaveDTO saveDTO) {
        ClientDTO clientDTO = clientService.saveClient(saveDTO);
        return Results.ok(clientDTO);
    }

    @Override
    public Result<Boolean> remove(String clientId) {
        boolean removed = clientService.remove(clientId);
        return Results.noContent(removed);
    }

    @Override
    public Result<ClientDTO> updateSecret(String clientId) {
        ClientDTO clientDTO = clientService.updateSecret(clientId);
        return Results.ok(clientDTO);
    }

    @Override
    public Result<ClientDTO> get(String clientId) {
        ClientDTO client = clientService.get(clientId);
        return Results.ok(client);
    }

    @Override
    public Result<Page<ClientDTO>> page(Integer pageNumber, Integer pageSize, String sort) {
        SortablePageQuery sortablePageQuery = SortablePageQuery.buildWithPermittedKeyEnum(
                pageNumber, pageSize, sort, ClientPageSort.class);
        Page<ClientDTO> clientDTOPage = clientService.page(sortablePageQuery);
        return Results.ok(clientDTOPage);
    }

}
