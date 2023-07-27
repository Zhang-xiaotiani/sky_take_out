package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.entity.User;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author admin
 * @version 1.0.0
 * @ClassName ReportServiceImpl.java
 * @Description TODO
 * @createTime 2023年07月26日 14:58:00
 */
@Service
@Slf4j
public class ReportServiceImpl implements ReportService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private OrderMapper orderMapper;

    /**
     * 营业额统计    查询订单表
     *
     * @param begin
     * @param end
     * @return
     */
    public TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = getDateList(begin, end);
        List<Double> turnoverList = new ArrayList<>();
        //2、
        for (LocalDate date : dateList) {
            //查询data日期对应的营业额，营业额指状态已完成订单合集
            LocalDateTime beginTimeOfOneDay = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTimeOfOneDay = LocalDateTime.of(date, LocalTime.MAX);
            //select sum(amount) from orders where order_time > beginTimeOfOneDay and order_time < endTimeOfOneDay and status = 5
            Map m = new HashMap();
            m.put("begin", beginTimeOfOneDay);
            m.put("end", endTimeOfOneDay);
            m.put("status", Orders.COMPLETED);
            Double turnover = orderMapper.sumByMap(m);
            turnover = turnover == null ? 0.0 : turnover;
            turnoverList.add(turnover);
        }

        TurnoverReportVO res = TurnoverReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .turnoverList(StringUtils.join(turnoverList, ","))
                .build();

        return res;
    }

    private List<LocalDate> getDateList(LocalDate begin, LocalDate end) {
        //1、datalist计算（罗列日期）
        List<LocalDate> dateList = new ArrayList<>();

        dateList.add(begin);
        while (!begin.equals(end)) {
            //日期计算
            begin = begin.plusDays(1);
            dateList.add(begin);
        }
        return dateList;
    }

    /**
     * 用户统计接口
     *
     * @param begin
     * @param end
     * @return
     */
    @Override
    public UserReportVO getUserStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = getDateList(begin, end);

        //SELECT COUNT(*) AS total FROM user WHERE create_date <= '2023-07-26'
        List<Integer> totalUserList = new ArrayList<>();
        //SELECT COUNT(*) AS total FROM user WHERE create_date = '2023-07-26'
        List<Integer> newUserList = new ArrayList<>();

        for (LocalDate date : dateList) {
            LocalDateTime dateMaxOfOneDay = LocalDateTime.of(date, LocalTime.MAX);
            LocalDateTime dateMinOfOneDay = LocalDateTime.of(date, LocalTime.MIN);
            Integer newUserCount = getUserCount(dateMinOfOneDay, dateMaxOfOneDay);
            newUserList.add(newUserCount);
            Integer UserCount = getUserCount(null, dateMaxOfOneDay);
            totalUserList.add(UserCount);
        }

        return UserReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .totalUserList(StringUtils.join(totalUserList, ","))
                .newUserList(StringUtils.join(newUserList, ","))
                .build();
    }

    private Integer getUserCount(LocalDateTime begin, LocalDateTime end) {
        Map map = new HashMap();
        map.put("begin", begin);
        map.put("end", end);
        return userMapper.getUserCount(map);
    }

    /**
     * - 根据起始时间和终止时间查询订单
     * - 有效订单指状态为 “已完成” 的订单
     * - 基于可视化报表的折线图展示订单数据，X轴为日期，Y轴为订单数量
     * - 根据时间选择区间，展示每天的订单总数和有效订单数
     * - 展示所选时间区间内的有效订单数、总订单数、订单完成率，订单完成率 = 有效订单数 / 总订单数 * 100%
     *
     * @param begin
     * @param end
     * @return
     */
    @Override
    public OrderReportVO getOrderStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = getDateList(begin, end);

        List<Integer> totalOrdersList = new ArrayList<>();//每日订单数量
        List<Integer> completedOrdersList = new ArrayList<>();//每日完成订单数量

        for (LocalDate date : dateList) {
            LocalDateTime dateMaxOfOneDay = LocalDateTime.of(date, LocalTime.MAX);
            LocalDateTime dateMinOfOneDay = LocalDateTime.of(date, LocalTime.MIN);

            Integer completedOrders = getOrdersCount(dateMinOfOneDay, dateMaxOfOneDay, Orders.COMPLETED);
            Integer totalOrders = getOrdersCount(dateMinOfOneDay, dateMaxOfOneDay, null);
            totalOrdersList.add(totalOrders);
            completedOrdersList.add(completedOrders);
        }

        Integer totalOrderCount = totalOrdersList.stream().reduce(Integer::sum).get();//全部订单数量
        Integer validOrderCount = completedOrdersList.stream().reduce(Integer::sum).get();//全部有效订单
        Double orderCompletionRate = totalOrderCount == 0 ? 0.0 : validOrderCount.doubleValue() / totalOrderCount;

        return OrderReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .orderCountList(StringUtils.join(totalOrdersList, ","))//每日订单总数
                .validOrderCountList(StringUtils.join(completedOrdersList, ","))//每日完成订单数
                .totalOrderCount(totalOrderCount)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .build();
    }

    private Integer getOrdersCount(LocalDateTime begin, LocalDateTime end, Integer status) {
        Map map = new HashMap();
        map.put("begin", begin);
        map.put("end", end);
        map.put("status", status);
        return orderMapper.getUserCount(map);
    }

    /**
     * 查询销量排名top10
     * - 根据时间选择区间，展示销量前10的商品（包括菜品和套餐）
     * - 基于可视化报表的柱状图降序展示商品销量
     * - 此处的销量为商品销售的份数
     *
     * @param begin
     * @param end
     * @return
     */
    @Override
    public SalesTop10ReportVO getSalesTop10(LocalDate begin, LocalDate end) {
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);
        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);

        List<GoodsSalesDTO> goodsSalesDTOList = orderMapper.getSalesTop10(beginTime, endTime);

        //商品名称列表，以逗号分隔，例如：鱼香肉丝,宫保鸡丁,水煮鱼
        String nameList = StringUtils.join(goodsSalesDTOList.stream().map(GoodsSalesDTO::getName).collect(Collectors.toList()), ",");
        //销量列表，以逗号分隔，例如：260,215,200
        String numberList = StringUtils.join(goodsSalesDTOList.stream().map(GoodsSalesDTO::getNumber).collect(Collectors.toList()), ",");

        return SalesTop10ReportVO.builder()
                .nameList(nameList)
                .numberList(numberList)
                .build();
    }
}
