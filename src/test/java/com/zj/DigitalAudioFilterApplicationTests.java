package com.zj;

import com.zj.mapper.TalkInfoMapper;
import com.zj.po.TalkInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class DigitalAudioFilterApplicationTests {

    @Autowired
    private TalkInfoMapper talkInfoMapper;

    @Test
    void contextLoads() {
        TalkInfo talkInfos = talkInfoMapper.selectById(1);
        System.out.println(talkInfos);
    }

}
