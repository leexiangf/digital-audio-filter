package com.zj;

import com.zj.mapper.TalkInfoMapper;
import com.zj.service.impl.TalkSoundServiceImpl;
import com.zj.utils.DetectQuietUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
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
            log.info("the fileName:"+tempUrl);
        }
        if(tempUrl.startsWith("data")||tempUrl.startsWith("./data")){
            String[] split = tempUrl.split("data/");
            log.info("the split first :"+split[0]);
            log.info("the split second :"+split[1]);
            tempUrl= DetectQuietUtil.PARENT_URL_PATH+split[1];
        }
        log.info("the LastFileName:"+tempUrl);
    }

}
