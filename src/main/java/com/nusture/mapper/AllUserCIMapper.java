package com.nusture.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nusture.pojo.AllUserCI;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AllUserCIMapper extends BaseMapper<AllUserCI> {
    List<AllUserCI> selectAllUserCI(String familyCode);
}
