package com.nusture.service;

import com.nusture.pojo.Cost;
import com.nusture.pojo.Income;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Nusture
 * @since 2021-12-28
 */
public interface IncomeService extends IService<Income> {
    BigDecimal getTodayIncome(String familyCode);

    List<Map<String, Object>> getIncomeByType(String familyCode);

    List<Map<String, Object>> getIncomeByTypeWater(String familyCode);

    Map<String, Object> getAllIncome(int id, String incomeType, String date, String account, String note, int currentPage, int pageSize);

    int changeIncome(int id, BigDecimal money, String incomeType, String account, String note);

    int deleteIncome(int id);

    int addIncome(int userId, BigDecimal money, String incomeType, String account, String note);

    Income getIncomeById(int id);
}
