package com.sky.mapper;

import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author admin
 * @version 1.0.0
 * @ClassName OrderDetailMapper.java
 * @Description TODO
 * @createTime 2023年07月25日 09:43:00
 */
@Mapper
public interface OrderDetailMapper {
    /**
     * 批量插入订单明细数据
     * @param orderDetails
     */
    void insertBatch(List<OrderDetail> orderDetails);

    /**
     * 根据订单id获取订单详细数据
     * @param id
     * @return
     */
    @Select("select * from order_detail where order_id = #{id} ")
    List<OrderDetail> getByOrderId(Long id);
}
