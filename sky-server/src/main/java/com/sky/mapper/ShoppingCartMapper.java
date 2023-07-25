package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import com.sky.service.ShoppingCartService;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author admin
 * @version 1.0.0
 * @ClassName ShoppingCartMapper.java
 * @Description TODO
 * @createTime 2023年07月24日 16:52:00
 */
@Mapper
public interface ShoppingCartMapper {

    /**
     *
     * 动态条件查询
     * @param shoppingCart
     * @return
     */
    List<ShoppingCart> list(ShoppingCart shoppingCart);

    /**
     * 更改购物车数量
     * @param shoppingCart
     */
    @Update("update shopping_cart set number = #{number} where id = #{id};")
    void updateNumberById(ShoppingCart shoppingCart);

    /**
     * 插入购物车
     * @param shoppingCart
     */
    @Insert("insert into shopping_cart " +
            "(name, user_id, dish_id, setmeal_id, dish_flavor, number, amount, image, create_time) " +
            " values " +
            "(#{name},#{userId},#{dishId},#{setmealId},#{dishFlavor},#{number},#{amount},#{image},#{createTime})")
    void insert(ShoppingCart shoppingCart);

    /**
     * 根据用户id删除购物车（Redis）
     * @param userId
     */
    @Delete("delete from shopping_cart where user_id = #{userId};")
    void deleteByUserId(Long userId);

    /**
     * 批量插入购物车数据
     *
     * @param shoppingCartList
     */
    void insertBatch(List<ShoppingCart> shoppingCartList);
}
