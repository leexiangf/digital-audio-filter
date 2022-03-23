package com.zj.mapper;

import com.zj.po.TalkSoundEntity;
import com.zj.po.TalkStatisticsEntity;

import java.util.List;

/**
 * @Classname TalkSoundMapper
 * @Description
 * @Date 2022/3/22 15:03
 * @Created by lxf
 */
public interface TalkSoundMapper {

    int addOne(TalkSoundEntity talkSoundEntity);
    void insertBatch(List<TalkStatisticsEntity> tss);
}
