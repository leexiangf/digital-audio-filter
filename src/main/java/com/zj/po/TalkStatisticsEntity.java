package com.zj.po;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName : TalkStatisticsEntity
 * @Description
 * @Date 2022/3/22 23:19
 * @Created lxf
 */
@Data
public class TalkStatisticsEntity {

    @ExcelProperty("电话型号")
    private String phoneType;
    @ExcelProperty("app版本")
    private String appVersion;
    @ExcelProperty("有录音文件数量")
    private int hasRecord;
    @ExcelProperty("无录音文件数量")
    private int noRecord;
    @ExcelProperty("录音文件有声音")
    private int hasSound;
    @ExcelProperty("录音文件无声音")
    private int noSound;
    @ExcelProperty("有声音占比")
    private double proportion;
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;
}
