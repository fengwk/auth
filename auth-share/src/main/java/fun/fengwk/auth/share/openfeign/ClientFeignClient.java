package fun.fengwk.auth.share.openfeign;

import fun.fengwk.auth.share.model.ClientCreateDTO;
import fun.fengwk.auth.share.model.ClientDTO;
import fun.fengwk.auth.share.model.ClientSaveDTO;
import fun.fengwk.convention4j.api.page.Page;
import fun.fengwk.convention4j.api.result.Result;
import org.springframework.web.bind.annotation.*;

/**
 * @author fengwk
 */
@RequestMapping("/oauth2/client")
public interface ClientFeignClient {

    @PostMapping
    Result<ClientDTO> create(@RequestBody ClientCreateDTO createDTO);

    @PutMapping
    Result<ClientDTO> update(@RequestBody ClientSaveDTO saveDTO);

    @DeleteMapping
    Result<Boolean> remove(@RequestParam("clientId") Long clientId);

    @PatchMapping("/secret")
    Result<ClientDTO> updateSecret(@RequestParam("clientId") Long clientId);

    @GetMapping("/{clientId}")
    Result<ClientDTO> get(@PathVariable("clientId") Long clientId);

    @GetMapping("/page")
    Result<Page<ClientDTO>> page(
        @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
        @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize,
        @RequestParam(value = "sort", defaultValue = "clientId") String sort);

}
