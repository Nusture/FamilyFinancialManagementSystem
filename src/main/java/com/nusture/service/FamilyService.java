package com.nusture.service;

import com.nusture.pojo.Family;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.Date;
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
public interface FamilyService extends IService<Family> {
    List<Map<String,Object>> getBaseMoneyAll(String familyCode);

    Map<String,String> getMoneyByCode(String familyCode);

    int changeMoney(String familyCode, BigDecimal money);

    int addBaseMoney(String familyCode, BigDecimal money);
}
