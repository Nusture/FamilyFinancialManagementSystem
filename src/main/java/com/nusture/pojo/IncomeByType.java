package com.nusture.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
public class IncomeByType {
    private BigDecimal incomeMoney;
    private String incomeType;
    private String userId;
    private String familyCode;
}
