package com.nusture.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nusture.pojo.MoneyChange;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MoneyChangeMapper extends BaseMapper<MoneyChange> {
    List<MoneyChange> selectMoneyChange(String familyCode);
}
