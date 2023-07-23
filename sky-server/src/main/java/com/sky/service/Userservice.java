package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;

/**
 * @author zym
 * @version 1.0.0
 * @ClassName Userservice.java
 * @Description TODO
 * @createTime 2023年07月23日 19:45:00
 */
public interface Userservice {

    /**
     * 微信登录
     * @param userLoginDTO
     * @return
     */
    User wxLogin(UserLoginDTO userLoginDTO);
}
