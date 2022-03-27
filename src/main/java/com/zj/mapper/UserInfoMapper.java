package com.zj.mapper;

import com.zj.po.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName : UserInfoMapper
 * @Description TODO
 * @Date 2022/3/27 14:31
 * @Created lxf
 */
public interface UserInfoMapper {

    User selectByUserId(@Param("userId") String userId);

    User selectByUserName(@Param("userName") String userName);

    List<User> selectAll();
}
