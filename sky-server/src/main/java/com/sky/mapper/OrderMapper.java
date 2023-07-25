package com.sky.mapper;

import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author admin
 * @version 1.0.0
 * @ClassName OrderMapper.java
 * @Description TODO
 * @createTime 2023年07月25日 09:37:00
 */
@Mapper
public interface OrderMapper {

    /**
     * 插入订单数据
     * 订单表 和 订单明细表
     * @param orders
     * @return
     */
    void insert(Orders orders);
}
