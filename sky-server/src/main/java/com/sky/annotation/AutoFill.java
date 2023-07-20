package com.sky.annotation;

import com.sky.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**1、自定义注解AutoFill，用于标识需要进行公共字段自动填充的方法
 * @author admin
 * @version 1.0.0
 * @ClassName AutoFill.java
 * @Description 自定义注解，用于某个方法需要进行功能字段自动填充处理
 *
 * @createTime 2023年07月20日 17:09:00
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFill {
    //指定数据库操作类型：UPDATE INSERT
    OperationType value();
}
