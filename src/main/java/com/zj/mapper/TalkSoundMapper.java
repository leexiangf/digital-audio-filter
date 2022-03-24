package com.zj.mapper;

import com.zj.po.TalkSoundEntity;

import java.util.List;
import com.zj.po.TalkSoundStatisticsEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Classname TalkSoundMapper
 * @Description
 * @Date 2022/3/22 15:03
 * @Created by lxf
 */
public interface TalkSoundMapper {

    int addOne(TalkSoundEntity talkSoundEntity);

    List<TalkSoundStatisticsEntity> talkSoundStatistics();

    int insertBatch(@Param("tss") List<TalkSoundStatisticsEntity> tss);
}
