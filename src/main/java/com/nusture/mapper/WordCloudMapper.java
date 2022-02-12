package com.nusture.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nusture.pojo.WordCloud;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WordCloudMapper extends BaseMapper<WordCloud> {
    List<WordCloud> selectWordCloud(String familyCode);
}
