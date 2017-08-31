package com.dcdt.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Created by LiRong on 2017/7/10.
 */
@Aspect
@Component
public class LogAspect {
    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    /**
     * 前置通知：在某连接点之前执行的通知，但这个通知不能阻止连接点前的执行
     *
     * @param jp 连接点：程序执行过程中的某一行为
     */
    public void doBefore(JoinPoint jp) {
        logger.info("[{}].[{}].({}) execute",
                jp.getTarget().getClass().getName(),
                jp.getSignature().getName(),
                Arrays.toString(jp.getArgs())
        );
    }

    /**
     * 环绕通知：包围一个连接点的通知，可以在方法的调用前后完成自定义的行为，也可以选择不执行。
     * 类似web中Servlet规范中Filter的doFilter方法。
     *
     * @param pjp 当前进程中的连接点
     * @return
     */
    public Object doAround(ProceedingJoinPoint pjp) {
        long Time = System.currentTimeMillis();
        Object result = null;
        try {
            result = pjp.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
            logger.info(e + "");
        }
        return result;
    }

    /**
     * 后置通知
     *
     * @param jp
     */
    public void doAfter(JoinPoint jp) {
        logger.info("[{}].[{}] end",
                jp.getTarget().getClass().getName(),
                jp.getSignature().getName()
        );
    }
}
