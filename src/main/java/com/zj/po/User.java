package com.zj.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName : UserInfo
 * @Description TODO
 * @Date 2022/3/27 14:26
 * @Created lxf
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {

    private long id;

    private String userId;

    private String username;

    private String password;

    private int tel;

    private String role;



}
