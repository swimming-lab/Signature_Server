package com.github.bestheroz.sample.web.tablevo.samplecodemst;

import com.github.bestheroz.standard.common.tablevo.SqlForTableDAO;
import com.github.bestheroz.standard.common.tablevo.SqlForTableVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Mapper
@Repository
public interface TableSampleCodeMstDAO extends SqlForTableDAO {
    @SelectProvider(type = SqlForTableVO.class, method = SqlForTableVO.SELECT)
    List<TableSampleCodeMstVO> getList(final TableSampleCodeMstVO vo, final Set<String> whereKeys, final String orderByColumns);

    @SelectProvider(type = SqlForTableVO.class, method = SqlForTableVO.SELECT_ONE)
    TableSampleCodeMstVO getVO(final TableSampleCodeMstVO vo, final Set<String> whereKeys);
}
