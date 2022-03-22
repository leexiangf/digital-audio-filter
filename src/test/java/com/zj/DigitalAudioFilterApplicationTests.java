package com.zj;

import com.alibaba.excel.EasyExcel;
import com.zj.mapper.TalkInfoMapper;
import com.zj.po.TalkStatisticsEntity;
import com.zj.service.impl.TalkSoundServiceImpl;
import com.zj.utils.DetectQuietUtil;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

@SpringBootTest

class DigitalAudioFilterApplicationTests {

    @Autowired
    TalkSoundServiceImpl soundService;

    private String tempUrl=
            "data/2021/202112/20211220/20211220184015750cut.mp3";

    @Test
    void contextLoads() {
       // System.out.println(soundService.talkSoundCheck(tempUrl));
        if(tempUrl.endsWith("cut.mp3")){
            String[] cuts = tempUrl.split("cut");
            tempUrl = cuts[0]+cuts[1];

        }
        if(tempUrl.startsWith("data")||tempUrl.startsWith("./data")){
            String[] split = tempUrl.split("data/");


            tempUrl= DetectQuietUtil.PARENT_URL_PATH+split[1];
        }

    }

    @Test
    void easyPoiTest(){
        ArrayList<TalkStatisticsEntity> list = new ArrayList<>();
        String fileName=DetectQuietUtil.PARENT_FILE_PATH+"通话录音统计表.xlsx";
        EasyExcel.write(fileName, TalkStatisticsEntity.class).sheet().doWrite(list);
    }

}
