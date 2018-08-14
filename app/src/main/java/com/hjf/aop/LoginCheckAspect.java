package com.hjf.aop;

import com.hjf.test.R;
import com.hjf.util.NotifyUtil;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class LoginCheckAspect {

    private static final int TIME_TAG = R.id.click_time;
    private static final int CLICK_DELAY_TIME_MILLIS = 2000;

    // 方法切入点，使用正则表达式指定
    @Pointcut("execution(@org.hjf.annotation.aspect.LoginCheck * *(..))")
    public void methodAnnotated() {

    }

    // 在连接点进行方法替换
    @Around("methodAnnotated()")
    public void aroundJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        boolean isLogin = false;
        if (!isLogin) {
            NotifyUtil.toast("请先登录");
            return;
        }
        // 执行原方法
        joinPoint.proceed();
    }
}
