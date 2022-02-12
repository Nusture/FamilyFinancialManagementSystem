package com.nusture.service;

import com.nusture.pojo.Cost;
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
public interface CostService extends IService<Cost> {
    BigDecimal getTodayCost(String familyCode);

    List<Map<String, Object>> getCostByType(String familyCode);

    List<Map<String, Object>> getCostByTypeWater(String familyCode);

    Map<String, Object> getAllCost(int id, String costType,String date, String account, String note, int currentPage, int pageSize);

    int changeCost(int id, BigDecimal money, String costType, String account, String note);

    int deleteCost(int id);

    int addCost(int userId, BigDecimal money, String costType, String account, String note);

    Cost getCostById(int id);



}
