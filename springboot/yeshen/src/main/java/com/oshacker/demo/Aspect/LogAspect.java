package com.oshacker.demo.Aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;

//面向切面，所有业务都要处理的业务，即在调用IndexController和
//SettingController的所有方法前后都要调用的方法
@Aspect
@Component
public class LogAspect {
    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Before("execution(* com.oshacker.demo.controller.*Controller.*(..))")
    public void beforeMethod(JoinPoint joinPoint) {//切点
        StringBuilder sb=new StringBuilder();
        for (Object arg:joinPoint.getArgs()) {//路径参数+请求参数
            sb.append(arg+" ");
        }
        logger.info("before method "+new Date()+" "+sb.toString());//可以访问/profile/admin/1?type=xx&page=1进行验证
    }

    @After("execution(* com.oshacker.demo.controller.*Controller.*(..))")
    public void afterMethod() {
        logger.info("after method "+new Date());
    }
}
