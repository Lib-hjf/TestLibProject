package com.hjf.aop;

import android.view.View;

import com.hjf.test.R;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.hjf.log.LogUtil;

import java.util.Calendar;

@Aspect
public class SingleClickAspect {

    private static final int TIME_TAG = R.id.click_time;
    private static final int CLICK_DELAY_TIME_MILLIS = 2000;

    // 方法切入点，使用正则表达式指定
    @Pointcut("execution(@org.hjf.aop.SingleClick * *(..))")
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
            LogUtil.e("SingleClickAspect - Not found click View");
            return;
        }
        Object tag = view.getTag(TIME_TAG);
        long lastClickTime = tag == null ? 0 : (long) tag;
        long currClickTime = Calendar.getInstance().getTimeInMillis();
        if (currClickTime - lastClickTime < CLICK_DELAY_TIME_MILLIS) {
            LogUtil.e("SingleClickAspect - Click time interval too short.");
            return;
        }
        view.setTag(TIME_TAG, currClickTime);
        // 执行原方法
        joinPoint.proceed();
    }
}
