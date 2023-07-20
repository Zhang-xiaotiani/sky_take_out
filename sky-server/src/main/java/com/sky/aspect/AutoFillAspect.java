package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.Joinpoint;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/*    2、自定义切面类AutoFillAspect，统一拦截加入了 AutoFill 注解的方法，通过反射为公共字段赋值
 * @author admin
 * @version 1.0.0
 * @ClassName AutoFillAspect.java
 * @Description 自定义切面，实现公共字段自动填充处理逻辑
 * @createTime 2023年07月20日 17:16:00
 */
@Aspect
@Component
@Slf4j
public class AutoFillAspect {
//    定义切入点和通知
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointCut(){}

//    定义通知(前置通知),在通知中进行公共字段赋值
    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint){
        log.info("开始进行公共字段的填充...");

//        获取当前被拦截的方法上的数据库操作类型
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();//方法签名
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);//获得方法上的注解对象
        OperationType operationType = autoFill.value();//获得操作类型

//        获取被拦截方法的参数（实体）
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0){
            return;
        }

        Object entity = args[0];

//        准备赋值的数据（时间和用户id）
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();

//        根据当前不同的操作类型，对相应的属性赋值（update 和 Insert）
        if (operationType == OperationType.INSERT){
            try {
                Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                //通过反射为对象赋值
                setCreateTime.invoke(entity,now);
                setCreateUser.invoke(entity,currentId);
                setUpdateUser.invoke(entity,currentId);
                setUpdateTime.invoke(entity,now);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if (operationType == OperationType.UPDATE){
            try {
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                //通过反射为对象赋值
                setUpdateUser.invoke(entity,currentId);
                setUpdateTime.invoke(entity,now);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
