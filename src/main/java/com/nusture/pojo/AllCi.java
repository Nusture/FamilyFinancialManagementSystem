package com.nusture.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class AllCi {
    private BigDecimal costMoney;
    private BigDecimal incomeMoney;
    private String familyCode;
    private Date createTime;


}
