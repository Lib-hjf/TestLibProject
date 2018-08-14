package com.hjf.aop;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import com.hjf.MyApp;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.hjf.activity.BaseActivity;
import org.hjf.annotation.aspect.PermissionCheck;
import org.hjf.log.LogUtil;

import java.util.ArrayList;
import java.util.List;

@Aspect
public class PermissionCheckAspect {

    private static final int REQUEST_CODE_PERMISSION = 1021;

    // 在连接点进行方法替换
    @Around("execution(@org.hjf.annotation.aspect.PermissionCheck * *(..))  && @annotation(permission)")
    public void aroundJoinPoint(ProceedingJoinPoint joinPoint, PermissionCheck permission) throws Throwable {

        // 1. 判断当前手机API版本是否 >= 6.0
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            LogUtil.e("Build.VERSION.SDK_INT = {0}", Build.VERSION.SDK_INT);
            // 执行原方法
            joinPoint.proceed();
            return;
        }

        // 2.1 过滤所有的权限申请，将目前没有的权限记录
        List<String> toPermissionList = new ArrayList<>();
        for (String permissionStr : permission.value()) {
            if (ContextCompat.checkSelfPermission(MyApp.getContext(), permissionStr) == PackageManager.PERMISSION_DENIED) {
                toPermissionList.add(permissionStr);
            }
        }
        // 2.2 如果为空，表示申请权限已经获取
        if (toPermissionList.size() == 0) {
            // 执行原方法
            joinPoint.proceed();
            LogUtil.e("PermissionCheckAspect - permission.length = 0");
            return;
        }

        // 3.1 开始请求授权
        final String[] toPermissions = toPermissionList.toArray(new String[toPermissionList.size()]);
        LogUtil.e("PermissionCheckAspect - permission.length = " + toPermissions.length);
        // 3.2 判断是否完美支持注解AOP权限申请功能，（能否获取授权结果回调）
        final Activity topActivity = MyApp.getTopActivity();
        // 能获取回调
        if (topActivity instanceof BaseActivity) {
            final ProceedingJoinPoint joinPointFil = joinPoint;
            ((BaseActivity) topActivity).setPermissionsResultCallback(REQUEST_CODE_PERMISSION, new ActivityCompat.OnRequestPermissionsResultCallback() {
                @Override
                public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
                    // 若发现有无授权情况，不再执行
                    for (int grantResultCode : grantResults) {
                        if (grantResultCode != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                    }
                    // 获取所有权限，执行原方法
                    try {
                        joinPointFil.proceed();
                    } catch (Throwable ignored) {
                    }
                }
            });
        }
        // 不会获取回调
        else {
            /**需要覆写方法 {@link FragmentActivity#onRequestPermissionsResult(int, String[], int[])} */
            LogUtil.e("PermissionCheckAspect - 非BaseActivity对象，无法监听授权结果回调，无法完美支持注解AOP授权方式");
        }
        // 3.3 获取授权弹框提示
        new AlertDialog.Builder(MyApp.getTopActivity())
                .setTitle("提示")
                .setMessage("软件正常使用需要申请权限。")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @TargetApi(Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        topActivity.requestPermissions(toPermissions, REQUEST_CODE_PERMISSION);
                    }
                }).create()
                .show();
    }
}
