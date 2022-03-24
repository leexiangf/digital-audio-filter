package com.zj.service.impl;

import com.mysql.jdbc.StringUtils;
import com.zj.mapper.TalkInfoMapper;
import com.zj.mapper.TalkSoundMapper;
import com.zj.po.ResultDBDTO;
import com.zj.po.TalkInfo;
import com.zj.po.TalkSoundEntity;
import com.zj.po.TalkSoundStatisticsEntity;
import com.zj.service.TalkSoundService;
import com.zj.utils.DetectQuietUtil;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sound.sampled.UnsupportedAudioFileException;
import javax.xml.ws.soap.Addressing;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Classname TalkSoundServiceImpl
 * @Description
 * @Date 2022/3/22 14:22
 * @Created by lxf
 */
@Service
@Slf4j
public class TalkSoundServiceImpl implements TalkSoundService {


    private final  static  String  PARENT_FILE_PATH="./data/";

    private final  static  String  PARENT_URL_PATH="https://call-file-online.oss-cn-hangzhou.aliyuncs.com/";



    private String  tempFile01 ="./data/有声音.mp3";
    private String  tempFile02 ="./data/无声音.mp3";

    @Autowired
    TalkInfoMapper talkInfoMapper;

    @Autowired
    TalkSoundMapper talkSoundMapper;

    /**
     * 检测一个文件是否有声音，并入库
     * @param fileName  文件路径
     * @return
     */
    @Override
    public int talkSoundCheck(String fileName)  {
         String srcUrl = fileToUrl(fileName);
         TalkInfo talkInfo = talkInfoMapper.selectByFileName(srcUrl);
         if(null==talkInfo || StringUtils.isNullOrEmpty(talkInfo.getTalkId())){
            return -1;
         }
         TalkSoundEntity talkSoundEntity = new TalkSoundEntity();

         talkSoundEntity.setTalkId(talkInfo.getTalkId());
         talkSoundEntity.setPhoneType(talkInfo.getPhoneType());
         talkSoundEntity.setSrcUrl(srcUrl);
         talkSoundEntity.setDurationSecond(talkInfo.getDurationSecond());

         if(!StringUtils.isNullOrEmpty(talkInfo.getTerminalInfo())){
             JSONObject jsonObject = new JSONObject(talkInfo.getTerminalInfo());
             String appVersion =(String)jsonObject.get("appVersion");
             talkSoundEntity.setAppVersion(appVersion);
         }

         //TODO  tempFile02 修改为 fileName
         ResultDBDTO resultDBDTO = DetectQuietUtil.loadFile(tempFile02);
        talkSoundEntity.setProportion(resultDBDTO.getProportion());
        talkSoundEntity.setSoundState(resultDBDTO.getProportion()>0 ? 1 : 0);
        talkSoundEntity.setRecordFileFlag(1);

        return  talkSoundMapper.addOne(talkSoundEntity);
    }

    /**
     * 统计通话录音信息
     * @return
     */
    @Override
    public List<TalkSoundStatisticsEntity> talkSoundStatistics() {
        List<TalkSoundStatisticsEntity> tss = talkSoundMapper.talkSoundStatistics();
         LocalDateTime now = LocalDateTime.now();
         long timeMillis = System.currentTimeMillis();
        tss.stream().forEach(s ->{
            s.setProportion(s.getHasRecord()==0? 0:(float)s.getHasSound()/s.getHasRecord());
            s.setCreateTime(timeMillis);
        });
        return tss;
    }


    /**
     *  将统计好的录音信息入库
     * @param tss
     * @return
     */
    @Override
    public int insertBatch(List<TalkSoundStatisticsEntity> tss) {
       return   talkSoundMapper.insertBatch(tss);
    }

    /**
     * 通过本地文件名查找对对应url
     * @param fileName
     * @return
     */
    private  String urlToFile(String srcUrl){
        String[] split = srcUrl.split(".com/");
        String newFile =PARENT_FILE_PATH+split[1];
        return newFile;
    }

    /**
     * 通过本地文件名查找对对应url
     * @param fileName
     * @return
     */
    private  String fileToUrl(String fileName){
        if(fileName.endsWith("cut.mp3")){
            String[] cuts = fileName.split("cut");
            fileName = cuts[0]+cuts[1];
            log.info("the fileName:"+fileName);
        }
        if(fileName.startsWith("./data") || fileName.startsWith("data")){
            String[] split = fileName.split("data/");
            return  fileName=PARENT_URL_PATH+split[1];
        }
        log.info("the LastFileName:"+fileName);
        return fileName;
    }
}
