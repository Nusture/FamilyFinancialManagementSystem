package com.nusture.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class CommitRecord {
    private Integer count;
    private Date createTime;
    private String familyCode;

}
