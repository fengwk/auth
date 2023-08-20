package fun.fengwk.auth.repo.mysql.mapper;

import fun.fengwk.auth.repo.mysql.model.ClientDO;
import fun.fengwk.automapper.annotation.AutoMapper;
import fun.fengwk.automapper.annotation.DynamicOrderBy;
import fun.fengwk.automapper.annotation.Selective;
import fun.fengwk.convention4j.springboot.starter.cache.annotation.CacheReadMethod;
import fun.fengwk.convention4j.springboot.starter.cache.annotation.CacheWriteMethod;
import fun.fengwk.convention4j.springboot.starter.cache.annotation.IdKey;
import fun.fengwk.convention4j.springboot.starter.cache.annotation.Key;
import fun.fengwk.convention4j.springboot.starter.cache.mapper.LongIdCacheMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author fengwk
 */
@AutoMapper
public interface ClientMapper extends LongIdCacheMapper<ClientDO> {

    @CacheWriteMethod
    int insertOnDuplicateKeyUpdate(ClientDO clientDO);

    @CacheWriteMethod
    int updateById(ClientDO clientDO);

    @CacheWriteMethod
    int updateByIdSelective(ClientDO clientDO);

    @CacheReadMethod
    ClientDO findByIdAndDeleted(@IdKey("id") @Param("id") long id, @Param("deleted") int deleted);

    @CacheReadMethod
    int countByNameAndDeleted(@Key("name") @Param("name") String name, @Param("deleted") int deleted);

    @CacheReadMethod
    long countByDeleted(@Param("deleted") int deleted);

    @CacheReadMethod
    List<ClientDO> pageByDeleted(@Param("offset") long offset, @Param("limit") int limit,
                                 @Param("deleted") int deleted,
                                 @Selective @DynamicOrderBy @Param("orderBy") String orderBy);

}
