package com.zj.po;

import lombok.Data;

/**
 * @Classname TalkSoundEntity
 * @Description  对应数据库tb_talk_soud表
 * @Date 2022/3/22 10:20
 * @Created by lxf
 */
@Data
public class TalkSoundEntity {
    private int id;

    private String talkId;

    private String appVersion;

    private String phoneType;

    private String srcUrl;

    private int durationSecond;

    private int soundState;

    private double proportion;

    private int recordFileFlag;
}
