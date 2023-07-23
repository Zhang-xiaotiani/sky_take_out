package com.sky.controller.user;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author admin
 * @version 1.0.0
 * @ClassName ShopController.java
 * @Description TODO
 * @createTime 2023年07月23日 15:46:00
 */
@RestController("userShopController")
@Slf4j
@Api(tags = "店铺相关接口")
@RequestMapping("/user/shop")
public class ShopController {
    public static final String KEY =  "SHOP_STATUS";

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 获取店铺的营业状态
     *
     * @return
     */
    @GetMapping("/status")
    @ApiOperation("获取店铺的营业状态")
    public Result<Integer> getStatus() {
        Integer status = (Integer) redisTemplate.opsForValue().get(KEY);
        log.info("获取店铺的营业状态:{}", status == 1 ? "营业中" : "打样中");
        return Result.success(status);
    }
}
