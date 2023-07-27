package com.sky.service;

import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;

import javax.servlet.http.HttpServletResponse;
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

    /**
     * 根据起始时间和终止时间查询订单
     * @param begin
     * @param end
     * @return
     */
    OrderReportVO getOrderStatistics(LocalDate begin, LocalDate end);

    /** 查询销量排名top10
     * - 根据时间选择区间，展示销量前10的商品（包括菜品和套餐）
     * - 基于可视化报表的柱状图降序展示商品销量
     * - 此处的销量为商品销售的份数
     * @param begin
     * @param end
     * @return
     */
    SalesTop10ReportVO getSalesTop10(LocalDate begin, LocalDate end);

    /** 导出Excel报表
     * 本质是文件下载Excel，无返回值
     * @return null
     */
    void exportBusinessData(HttpServletResponse response);
}
