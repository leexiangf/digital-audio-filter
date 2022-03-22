package com.zj.mapper;

import com.zj.po.TalkInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Classname TalkInfoMapper
 * @Description
 * @Date 2022/3/21 18:18
 * @Created by lxf
 */
public interface TalkInfoMapper {

    List<TalkInfo> selectAll();

    TalkInfo selectById(@Param("id") int id);

    TalkInfo selectByFileName(@Param("srcUrl") String fileName);
}
