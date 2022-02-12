package com.nusture.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
public class CostByType {
    private BigDecimal costMoney;
    private String costType;
    private String userId;
    private String familyCode;
}
