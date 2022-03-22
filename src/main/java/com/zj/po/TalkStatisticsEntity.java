package com.zj.po;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @ClassName : TalkStatisticsEntity
 * @Description
 * @Date 2022/3/22 23:19
 * @Created lxf
 */
@Data
public class TalkStatisticsEntity {

    //@Excel(name = "电话型号",orderNum = "0")
    @ExcelProperty("电话型号")
    private String phoneType;
    //@Excel(name = "有录音文件数量",orderNum = "1")
    @ExcelProperty("有录音文件数量")
    private int hasRecord;
    //@Excel(name = "无录音文件数量",orderNum = "2")
    @ExcelProperty("无录音文件数量")
    private int noRecord;
    //@Excel(name = "录音文件有声音",orderNum = "3")
    @ExcelProperty("录音文件有声音")
    private int hasSound;
    //@Excel(name = "录音文件无声音",orderNum = "4")
    @ExcelProperty("录音文件无声音")
    private int noSound;
    //@Excel(name = "有声音占比",orderNum = "5")
    @ExcelProperty("有声音占比")
    private double proportion;
}
