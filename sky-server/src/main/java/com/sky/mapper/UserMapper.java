package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

/**
 * @author admin
 * @version 1.0.0
 * @ClassName UserMapper.java
 * @Description TODO
 * @createTime 2023年07月23日 20:08:00
 */
@Mapper
public interface UserMapper {

    @Select("select * from user where openid = #{openid}")
    User getByOpenid(String openid);

    /**
     * 插入数据
     * @param user
     */
    void insert(User user);

    /**
     * 根据userid查询用户
     * @param userId
     * @return
     */
    @Select("select * from user where id = #{userId} ;")
    User getById(Long userId);

    /**
     * 根据起始时间和终止时间查询用户数量
     * @param map
     * @return
     */
//    @Select("SELECT COUNT(*) AS total FROM user WHERE create_time < #{end} and create_time > #{begin}")
    Integer getUserCount(Map map);
}
