package com.nusture.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class MoneyChange {
    private BigDecimal baseMoney;
    private String familyCode;
    private Date createTime;
}
