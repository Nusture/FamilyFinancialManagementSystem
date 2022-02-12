package com.nusture.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nusture.pojo.AllCi;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AllCiMapper extends BaseMapper<AllCi> {
    List<AllCi> selectAllCi(String familyCode);
}
