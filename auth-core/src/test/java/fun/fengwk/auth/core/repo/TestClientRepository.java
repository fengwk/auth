package fun.fengwk.auth.core.repo;

import fun.fengwk.auth.core.model.ClientBO;
import fun.fengwk.auth.share.constant.ClientPageSort;
import fun.fengwk.convention4j.api.page.Page;
import fun.fengwk.convention4j.api.page.Sort;
import fun.fengwk.convention4j.api.page.SortablePageQuery;
import fun.fengwk.convention4j.common.gson.GsonUtils;
import fun.fengwk.convention4j.common.page.Pages;
import fun.fengwk.convention4j.common.reflect.TypeToken;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author fengwk
 */
@Repository
public class TestClientRepository implements ClientRepository {

    private long id;
    private final Map<String, ClientBO> map = new HashMap<>();

    @Override
    public String generateId() {
        return String.valueOf(++id);
    }

    @Override
    public boolean add(ClientBO clientBO) {
        if (map.containsKey(clientBO.getId())) {
            return false;
        }
        clientBO = GsonUtils.fromJson(GsonUtils.toJson(clientBO), ClientBO.class);
        map.put(clientBO.getId(), clientBO);
        return true;
    }

    @Override
    public boolean updateById(ClientBO clientBO) {
        clientBO = GsonUtils.fromJson(GsonUtils.toJson(clientBO), ClientBO.class);
        map.put(clientBO.getId(), clientBO);
        return true;
    }

    @Override
    public boolean removeById(String id) {
        return map.remove(id) != null;
    }

    @Override
    public ClientBO getById(String id) {
        ClientBO clientBO = map.get(id);
        clientBO = GsonUtils.fromJson(GsonUtils.toJson(clientBO), ClientBO.class);
        return clientBO;
    }

    @Override
    public boolean existsByName(String name) {
        ClientBO clientBO = map.values().stream().filter(c -> c.getName().equals(name)).findFirst().orElse(null);
        return clientBO != null;
    }

    @Override
    public Page<ClientBO> page(SortablePageQuery sortablePageQuery) {
        List<ClientBO> list = map.values().stream().sorted((c1, c2) -> {
                    List<Sort> sorts = sortablePageQuery.getSorts();
                    if (CollectionUtils.isEmpty(sorts)) {
                        return c1.getId().compareTo(c2.getId());
                    }
                    for (Sort sort : sorts) {
                        ClientPageSort s = EnumUtils.getEnum(ClientPageSort.class, sort.getKey());
                        switch (s) {
                            case id:
                                int c = c1.getId().compareTo(c2.getId());
                                if (c != 0) {
                                    return sort.isAsc() ? c : -c;
                                }
                                break;
                            default:
                                throw new UnsupportedOperationException();
                        }
                    }
                    return 0;
                })
                .skip(Pages.queryOffset(sortablePageQuery))
                .limit(Pages.queryLimit(sortablePageQuery))
                .collect(Collectors.toList());
        list = GsonUtils.fromJson(GsonUtils.toJson(list), new TypeToken<List<ClientBO>>() {}.getType());
        return Pages.page(sortablePageQuery, list, map.size());
    }

}
