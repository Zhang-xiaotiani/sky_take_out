package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.entity.User;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
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
            Integer newUserCount =getUserCount(dateMinOfOneDay,dateMaxOfOneDay);
            newUserList.add(newUserCount);
            Integer UserCount =getUserCount(null,dateMaxOfOneDay);
            totalUserList.add(UserCount);
        }

        return UserReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .totalUserList(StringUtils.join(totalUserList, ","))
                .newUserList(StringUtils.join(newUserList, ","))
                .build();
    }
    private Integer getUserCount(LocalDateTime begin,LocalDateTime end){
        Map map = new HashMap();
        map.put("begin", begin);
        map.put("end", end);
        return userMapper.getUserCount(map);
    }
}
