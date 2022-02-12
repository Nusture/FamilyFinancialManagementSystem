package com.nusture.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nusture.pojo.CostByType;
import com.nusture.pojo.CostByWater;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CostByWaterMapper extends BaseMapper<CostByWater> {
    List<CostByWater> selectCostByWater(String familyCode);
}
