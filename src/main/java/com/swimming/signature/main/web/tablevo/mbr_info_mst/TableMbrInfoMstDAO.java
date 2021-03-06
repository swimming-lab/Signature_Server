package com.github.bestheroz.main.web.tablevo.mbr_info_mst;

import com.github.bestheroz.standard.common.tablevo.SqlForTableDAO;
import com.github.bestheroz.standard.common.tablevo.SqlForTableVO;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Mapper
@Repository
public interface TableMbrInfoMstDAO extends SqlForTableDAO {

    @SelectProvider(type = SqlForTableVO.class, method = SqlForTableVO.SELECT)
    List<TableMbrInfoMstVO> getList(final TableMbrInfoMstVO vo, final Set<String> whereKeys, final String orderByColumns);

    @SelectProvider(type = SqlForTableVO.class, method = SqlForTableVO.SELECT_ONE)
    TableMbrInfoMstVO getVO(final TableMbrInfoMstVO vo, final Set<String> whereKeys);

    @Override
    @InsertProvider(type = SqlForTableVO.class, method = SqlForTableVO.INSERT)
    <T> void insert(final T vo);
}
