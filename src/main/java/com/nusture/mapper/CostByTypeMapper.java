package com.nusture.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nusture.pojo.CostByType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CostByTypeMapper extends BaseMapper<CostByType> {
    List<CostByType> selectCostByType(String familyCode);
}
