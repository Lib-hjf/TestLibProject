package com.hjf.test.t_func;

import android.Manifest;
import android.view.View;

import com.hjf.test.R;
import com.hjf.util.NotifyUtil;

import com.hjf.base.activity.BaseFragment;
import org.hjf.annotation.aspect.LoginCheck;
import org.hjf.annotation.aspect.PermissionCheck;
import org.hjf.annotation.aspect.SingleClick;

/**
 * WiFi自动切换
 */
public class AOPDemoFragment extends BaseFragment implements View.OnClickListener {

    private int index = 0;

    public static BaseFragment newInstance() {
        AOPDemoFragment switchDemoFragment = new AOPDemoFragment();
        return switchDemoFragment;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.f_aop_test;
    }

    @Override
    public void bindView() {
        findViewById(R.id.btn_click).setOnClickListener(this);
        findViewById(R.id.btn_login_check).setOnClickListener(this);
        findViewById(R.id.btn_permission).setOnClickListener(this);
    }

    @Override
    @SingleClick
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_click:
                NotifyUtil.toast(++index + "- click");
                break;
            case R.id.btn_login_check:
                loginStatus();
                break;
            case R.id.btn_permission:
                permissionCheck();
                break;
        }
    }

    @PermissionCheck({
            Manifest.permission.CAMERA,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_SMS
    })
    private void permissionCheck() {
        NotifyUtil.toast("授权成功");
    }

    @LoginCheck
    private void loginStatus() {
        NotifyUtil.toast("用户已登录");
    }


}
