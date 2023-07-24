package com.sky.service;

import com.sky.dto.ShoppingCartDTO;

/**
 * @author admin
 * @version 1.0.0
 * @ClassName ShoppingCartService.java
 * @Description TODO
 * @createTime 2023年07月24日 16:44:00
 */
public interface ShoppingCartService {

    /**
     * 添加购物车
     * @param shoppingCartDTO
     */
    void addShoppingCart(ShoppingCartDTO shoppingCartDTO);
}
