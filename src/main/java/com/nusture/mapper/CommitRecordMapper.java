package com.nusture.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nusture.pojo.CommitRecord;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommitRecordMapper extends BaseMapper<CommitRecord> {
    List<CommitRecord> selectCommitRecord(String familyCode);
}
