package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author admin
 * @version 1.0.0
 * @ClassName OrderTask.java
 * @Description TODO
 * @createTime 2023年07月26日 10:51:00
 */
@Component
@Slf4j
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 处理超时订单
     * 每分钟触发一次？？？？？
     */
    @Scheduled(cron = "0 * * * * ?")
    public void processTimeOutOrder() {
        log.info("定时处理超时订单：{}", LocalDateTime.now());

        LocalDateTime time = LocalDateTime.now().plusMinutes(-15);
        //查询超时订单
        //select * from orders where status = ?(代付款) and order_time < ?(超过15分钟  --> 当前时间-15分钟？)
        List<Orders> ordersList = orderMapper.getByStatusAndOrderTimeLT(Orders.PENDING_PAYMENT, time);

        if (ordersList != null && ordersList.size() > 0){
            log.info("未付款且超时的订单数量：{}",ordersList.size());
            for (Orders orders : ordersList) {
                orders.setStatus(Orders.CANCELLED);
                orders.setCancelReason("订单超时，用户未付款");
                orders.setCancelTime(LocalDateTime.now());
                orderMapper.update(orders);
            }
        }
    }

    /**
     * 处理一直派送中订单
     * 每天凌晨一点触发一次？？？？？
     * 我觉得每小时一次更合理（）
     */
    @Scheduled(cron = "0 0 0/1 * * ? ")
//    @Scheduled(cron = "0/3 * * * * ?")
    public void processDeliveryOrder(){
        log.info("定时处理派送订单：{}", LocalDateTime.now());

        LocalDateTime time = LocalDateTime.now().plusHours(-1);

        List<Orders> ordersList = orderMapper.getByStatusAndOrderTimeLT(Orders.DELIVERY_IN_PROGRESS, time);

        if (ordersList != null && ordersList.size() > 0){
            log.info("一直派送中的订单数量：{}",ordersList.size());
            for (Orders orders : ordersList) {
                orders.setStatus(Orders.COMPLETED);
                orderMapper.update(orders);
            }
        }
    }
}
