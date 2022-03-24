package com.zj.service;

import com.zj.po.TalkSoundEntity;
import com.zj.po.TalkSoundStatisticsEntity;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.List;

/**
 * @Classname TalkSoundService
 * @Description
 * @Date 2022/3/22 14:21
 * @Created by lxf
 */
public interface TalkSoundService {

        public int talkSoundCheck(String fileName) ;

        public List<TalkSoundStatisticsEntity> talkSoundStatistics();

        int insertBatch(List<TalkSoundStatisticsEntity> tss);
}
