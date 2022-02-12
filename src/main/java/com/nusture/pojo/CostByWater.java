package com.nusture.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
public class CostByWater {
    private BigDecimal costMoney;
    private String costType;
    private String familyCode;
}
