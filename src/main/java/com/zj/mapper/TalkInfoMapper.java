package com.zj.mapper;

import com.zj.po.TalkInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Classname TalkInfoMapper
 * @Description TODO
 * @Date 2022/3/21 18:18
 * @Created by lxf
 */
@Repository
public interface TalkInfoMapper {

    List<TalkInfo> selectAll();
}
