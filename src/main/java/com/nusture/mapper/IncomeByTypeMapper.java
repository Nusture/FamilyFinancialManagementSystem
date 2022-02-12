package com.nusture.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nusture.pojo.CostByType;
import com.nusture.pojo.IncomeByType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncomeByTypeMapper extends BaseMapper<IncomeByType> {
    List<IncomeByType> selectIncomeByType(String familyCode);
}
