package com.zj;

import com.alibaba.excel.EasyExcel;
import com.zj.mapper.UserInfoMapper;
import com.zj.po.TalkSoundStatisticsEntity;
import com.zj.po.User;
import com.zj.service.impl.TalkSoundServiceImpl;
import com.zj.utils.DetectQuietUtil;

import com.zj.utils.JWTUtil;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class DigitalAudioFilterApplicationTests {

    @Autowired
    TalkSoundServiceImpl soundService;

    @Autowired
    UserInfoMapper userInfoMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    private String tempUrl=
            "data/2022/202203/20220322/A02087688093_1647930708471_220322143232152.aac";

    @Test
    void contextLoads() {
        System.out.println(soundService.talkSoundCheck(tempUrl));
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


    @Test
    void jsonTest(){
       /* List<UserInfo> userInfos =  userInfoMapper.selectAll();
        userInfos.forEach(System.out::println);*/
        User zs = userInfoMapper.selectByUserName("zs");
        System.out.println(zs);
    }

    @Test
    void EncoderTest(){
        //String encode = passwordEncoder.encode("111111");
        //System.out.println(encode);
        //String token = JWTUtil.createToken("张帅");
        //System.out.println(token);
        Claims claims = JWTUtil.parseToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiLlvKDluIUiLCJqdGkiOiJTdW4gTWFyIDI3IDE2OjAwOjA0IENTVCAyMDIyIiwibmJmIjoxNjQ4MzY4MDA0LCJpYXQiOjE2NDgzNjgwMDQsImV4cCI6MTY1MDk2MDAwNH0.29OeA14QossgDU07KTxfAyL1lf-XeikBY_GWfPawGjw");
        System.out.println(claims.getSubject());
    }
}
