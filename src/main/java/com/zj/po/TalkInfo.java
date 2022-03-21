package com.zj.po;

import javax.xml.crypto.Data;

/**
 * @Classname TalkInfo
 * @Description TODO
 * @Date 2022/3/21 17:48
 * @Created by lxf
 */

@lombok.Data
public class TalkInfo {

    private String talkId;

    private Data terminalInfo;

    private String phoneType;

    private String srcUrl;

}
