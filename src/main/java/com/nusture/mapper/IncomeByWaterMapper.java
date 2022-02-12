package com.nusture.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nusture.pojo.CostByWater;
import com.nusture.pojo.IncomeByWater;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncomeByWaterMapper extends BaseMapper<IncomeByWater> {
    List<IncomeByWater> selectIncomeByWater(String familyCode);
}
