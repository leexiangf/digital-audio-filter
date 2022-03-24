package com.zj;

import com.alibaba.excel.EasyExcel;
import com.zj.po.TalkSoundStatisticsEntity;
import com.zj.service.impl.TalkSoundServiceImpl;
import com.zj.utils.DetectQuietUtil;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest

class DigitalAudioFilterApplicationTests {

    @Autowired
    TalkSoundServiceImpl soundService;

    private String tempUrl=
            "data/2022/202203/20220322/A02087688093_1647930708471_220322143232152.aac";

    @Test
    void contextLoads() {
        System.out.println(soundService.talkSoundCheck(tempUrl));
     /*   if(tempUrl.endsWith("cut.mp3")){
            String[] cuts = tempUrl.split("cut");
            tempUrl = cuts[0]+cuts[1];

        }
        if(tempUrl.startsWith("data")||tempUrl.startsWith("./data")){
            String[] split = tempUrl.split("data/");


            tempUrl= DetectQuietUtil.PARENT_URL_PATH+split[1];
        }*/

    }

    @Test
    void easyPoiTest(){
        ArrayList<TalkSoundStatisticsEntity> list = new ArrayList<>();
        String fileName=DetectQuietUtil.PARENT_FILE_PATH+"通话录音统计表.xlsx";
        EasyExcel.write(fileName, TalkSoundStatisticsEntity.class).sheet().doWrite(list);
    }

    @Test
    void talkTest(){
         List<TalkSoundStatisticsEntity> tss = soundService.talkSoundStatistics();
         tss.forEach(System.out::println);
        System.out.println(soundService.insertBatch(tss));
    }

}
