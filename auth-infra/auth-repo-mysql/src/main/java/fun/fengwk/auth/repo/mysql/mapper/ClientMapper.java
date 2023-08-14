package fun.fengwk.auth.repo.mysql.mapper;

import fun.fengwk.auth.repo.mysql.model.ClientDO;
import fun.fengwk.automapper.annotation.AutoMapper;
import fun.fengwk.automapper.annotation.DynamicOrderBy;
import fun.fengwk.automapper.annotation.Selective;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author fengwk
 */
@AutoMapper
public interface ClientMapper {

    int insertOnDuplicateKeyUpdate(ClientDO clientDO);

    int updateById(ClientDO clientDO);

    int updateByIdSelective(ClientDO clientDO);

    ClientDO findByIdAndDeleted(@Param("id") long id, @Param("deleted") int deleted);

    int countByNameAndDeleted(@Param("name") String name, @Param("deleted") int deleted);

    long countByDeleted(@Param("deleted") int deleted);

    List<ClientDO> pageByDeleted(@Param("offset") long offset, @Param("limit") int limit,
                                 @Param("deleted") int deleted,
                                 @Selective @DynamicOrderBy @Param("orderBy") String orderBy);

}
