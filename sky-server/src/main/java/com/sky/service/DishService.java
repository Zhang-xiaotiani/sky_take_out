package com.sky.service;

import com.sky.dto.DishDTO;

/**
 * @author admin
 * @version 1.0.0
 * @ClassName DishService.java
 * @Description TODO
 * @createTime 2023年07月21日 09:39:00
 */
public interface DishService {

    /**
     * 新增菜品和对应的口味
     * @param dishDTO
     */
    void saveWithFlavor(DishDTO dishDTO);
}
