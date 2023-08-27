package fun.fengwk.auth.repo.mysql;

import com.google.common.collect.ImmutableMap;
import fun.fengwk.auth.core.model.ClientBO;
import fun.fengwk.auth.core.repo.ClientRepository;
import fun.fengwk.auth.repo.mysql.mapper.ClientMapper;
import fun.fengwk.auth.repo.mysql.model.ClientDO;
import fun.fengwk.auth.share.constant.ClientPageSort;
import fun.fengwk.convention4j.api.page.Page;
import fun.fengwk.convention4j.api.page.SortablePageQuery;
import fun.fengwk.convention4j.common.IntBool;
import fun.fengwk.convention4j.common.NullSafe;
import fun.fengwk.convention4j.common.StringUtils;
import fun.fengwk.convention4j.common.idgen.NamespaceIdGenerator;
import fun.fengwk.convention4j.common.page.Pages;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author fengwk
 */
@Repository
public class MysqlClientRepository implements ClientRepository {

    private static final ImmutableMap<String, String> sortFieldMap = ImmutableMap.of(
            ClientPageSort.id.name(), "id"
    );

    @Autowired
    private NamespaceIdGenerator<Long> idGenerator;
    @Autowired
    private ClientMapper clientMapper;

    @Override
    public String generateId() {
        return String.valueOf(idGenerator.next(getClass()));
    }

    @Override
    public boolean add(ClientBO clientBO) {
        if (clientBO == null) {
            return false;
        }
        ClientDO clientDO = ClientDO.bo2do(clientBO);
        return clientMapper.insertOnDuplicateKeyUpdate(clientDO) > 0;
    }

    @Override
    public boolean updateById(ClientBO clientBO) {
        if (clientBO == null) {
            return false;
        }
        ClientDO clientDO = ClientDO.bo2do(clientBO);
        return clientMapper.updateById(clientDO) > 0;
    }

    @Override
    public boolean removeById(String id) {
        if (!NumberUtils.isCreatable(id)) {
            return false;
        }
        ClientDO clientDO = ClientDO.forLogicDelete(NumberUtils.toLong(id));
        return clientMapper.updateByIdSelective(clientDO) > 0;
    }

    @Override
    public ClientBO getById(String id) {
        if (!NumberUtils.isCreatable(id)) {
            return null;
        }
        ClientDO clientDO = clientMapper.findByIdAndDeleted(NumberUtils.toLong(id), IntBool.FALSE);
        return NullSafe.map(clientDO, ClientDO::toBO);
    }

    @Override
    public boolean existsByName(String name) {
        if (StringUtils.isEmpty(name)) {
            return false;
        }
        return clientMapper.countByNameAndDeleted(name, IntBool.FALSE) > 0;
    }

    @Override
    public Page<ClientBO> page(SortablePageQuery sortablePageQuery) {
        if (sortablePageQuery == null) {
            return Pages.emptyPage();
        }
        List<ClientDO> clientPOs = clientMapper.pageByDeleted(
                Pages.queryOffset(sortablePageQuery), Pages.queryLimit(sortablePageQuery),
                IntBool.FALSE, Pages.formatSqlOrderBy(sortablePageQuery.getSorts(), sortFieldMap));
        long count = clientMapper.countByDeleted(IntBool.FALSE);
        return Pages.page(sortablePageQuery, clientPOs, count).map(ClientDO::toBO);
    }

}
