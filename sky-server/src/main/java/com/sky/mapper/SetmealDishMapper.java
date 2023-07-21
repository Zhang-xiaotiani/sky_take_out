package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
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

    /**
     * 批量保存套餐和菜品的关联关系
     * @param setmealDishes
     */
    void insertBatch(List<SetmealDish> setmealDishes);
}
