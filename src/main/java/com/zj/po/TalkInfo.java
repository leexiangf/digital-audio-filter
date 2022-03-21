package com.zj.po;


import lombok.Data;

/**
 * @Classname TalkInfo
 * @Description
 * @Date 2022/3/21 17:48
 * @Created by lxf
 */

@Data
public class TalkInfo {

    private int id;

    private String talkId;

    private String terminalInfo;

    private String phoneType;

    private String srcUrl;

    private int soundState;

    private double proportion;

}
