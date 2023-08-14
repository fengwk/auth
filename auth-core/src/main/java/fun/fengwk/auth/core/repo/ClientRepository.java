package fun.fengwk.auth.core.repo;

import fun.fengwk.auth.core.model.ClientBO;
import fun.fengwk.convention4j.api.page.Page;
import fun.fengwk.convention4j.api.page.SortablePageQuery;

/**
 * @author fengwk
 */
public interface ClientRepository {

    long generateId();

    boolean add(ClientBO clientBO);

    boolean updateById(ClientBO clientBO);

    boolean removeById(Long id);

    ClientBO getById(Long id);

    boolean existsByName(String name);

    Page<ClientBO> page(SortablePageQuery sortablePageQuery);

}
