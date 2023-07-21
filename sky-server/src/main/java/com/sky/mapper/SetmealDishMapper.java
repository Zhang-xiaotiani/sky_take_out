package com.sky.mapper;

import com.sky.entity.Setmeal;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author admin
 * @version 1.0.0
 * @ClassName SetmealDishMapper.java
 * @Description TODO
 * @createTime 2023年07月21日 15:32:00
 */
@Mapper
public interface SetmealDishMapper {
    /**
     * 根据菜品id查询相应的套餐id
     * @param dishIds
     * @return
     */
    List<Long> getSetmealDishIds(List<Long> dishIds);

    void update(Setmeal setmeal);
}
