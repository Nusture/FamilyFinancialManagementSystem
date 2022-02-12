package com.nusture.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
public class WordCloud {
    private Integer count;
    private String type;
    private String familyCode;
}
