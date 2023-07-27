package com.sky.service;

import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author admin
 * @version 1.0.0
 * @ClassName ReportService.java
 * @Description TODO
 * @createTime 2023年07月26日 14:58:00
 */
public interface ReportService {
    /**
     * 营业额统计
     *
     * @param begin
     * @param end
     * @return
     */
    TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end);

    /**
     * 用户统计接口
     * @param begin
     * @param end
     * @return
     */
    UserReportVO getUserStatistics(LocalDate begin, LocalDate end);
}
