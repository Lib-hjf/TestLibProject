package com.hjf.aop;

import android.view.View;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.hjf.util.R;
import org.hjf.util.log.LogUtil;

import java.util.Calendar;

@Aspect
public class SingleClickAspect {

    private static final int TIME_TAG = R.id.click_time;
    private static final int CLICK_DELAY_TIME_MILLIS = 600;

    // 方法切入点，使用正则表达式指定
    @Pointcut("execution(@org.hjf.util.aop.SingleClick * *(..))")
    public void methodAnnotated() {

    }

    // 在连接点进行方法替换
    @Around("methodAnnotated()")
    public void aroundJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        View view = null;
        for (Object obj : joinPoint.getArgs()) {
            if (obj instanceof View) {
                view = (View) obj;
            }
        }
        if (view == null) {
            LogUtil.d("view: " + null);
            return;
        }
        Object tag = view.getTag(TIME_TAG);
        long lastClickTime = tag == null ? 0 : (long) tag;
        LogUtil.d("lastClickTime: " + lastClickTime);
        long currClickTime = Calendar.getInstance().getTimeInMillis();
        LogUtil.d("currClickTime: " + currClickTime);
        if (currClickTime - lastClickTime >= CLICK_DELAY_TIME_MILLIS) {
            view.setTag(TIME_TAG, currClickTime);
            // 执行原方法
            joinPoint.proceed();
        }
    }
}
