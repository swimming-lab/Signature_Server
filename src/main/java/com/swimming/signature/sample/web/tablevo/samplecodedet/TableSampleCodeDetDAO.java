package com.github.bestheroz.sample.web.tablevo.samplecodedet;

import com.github.bestheroz.standard.common.tablevo.SqlForTableDAO;
import com.github.bestheroz.standard.common.tablevo.SqlForTableVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Mapper
@Repository
public interface TableSampleCodeDetDAO extends SqlForTableDAO {
    @SelectProvider(type = SqlForTableVO.class, method = SqlForTableVO.SELECT)
    List<TableSampleCodeDetVO> getList(final TableSampleCodeDetVO vo, final Set<String> whereKeys, final String orderByColumns);

    @SelectProvider(type = SqlForTableVO.class, method = SqlForTableVO.SELECT_ONE)
    TableSampleCodeDetVO getVO(final TableSampleCodeDetVO vo, final Set<String> whereKeys);
}
