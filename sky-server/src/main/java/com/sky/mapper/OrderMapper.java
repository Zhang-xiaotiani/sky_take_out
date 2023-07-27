package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.GoodsSalesDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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

    /**
     * 根据订单号查询订单
     * @param orderNumber
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 修改订单信息
     * @param orders
     */
    void update(Orders orders);

    /**
     * 分页条件查询并按下单时间排序
     * @param ordersPageQueryDTO
     */
    Page<Orders> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 根据id查询订单
     * @param id
     * @return
     */
    @Select("select * from orders where id = #{id} ;")
    Orders getById(Long id);

    /**
     *查询每个状态的订单数量
     * @param status
     * @return
     */
    @Select("select count(id) from orders where status = #{status}")
    Integer countStatus(Integer status);

    /**
     *根据订单状态和下单时间查询订单
     * @param status
     * @param orderTime
     */
    @Select("select * from orders where status = #{status} and order_time < #{orderTime}")
    List<Orders> getByStatusAndOrderTimeLT(Integer status, LocalDateTime orderTime);

    /**
     * 根据起始日期到最后日期以及状态查询      金额累加
     * @param map
     * @return
     */

    Double sumByMap(Map map);

    /**
     * 根据起始日期到最后日期以及状态查询订单数
     * @param map
     * @return
     */
    Integer getUserCount(Map map);

    /**
     * 查询商品销量排名
     * @param begin
     * @param end
     * @return
     */
    List<GoodsSalesDTO> getSalesTop10(LocalDateTime begin, LocalDateTime end);
}
