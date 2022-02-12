package com.nusture.pojo;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Nusture
 * @since 2021-12-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Income implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 收入类别
     */
    private String incomeType;

    /**
     * 收入金额
     */
    private BigDecimal incomeMoney;

    /**
     * 家庭码
     */
    private String familyCode;

    /**
     * 账户
     */
    private String account;

    /**
     * 描述
     */
    private String note;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 乐观锁
     */
    @Version
    private Integer version;

    /**
     * 创建时间
     */
      @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     */
      @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;


}
