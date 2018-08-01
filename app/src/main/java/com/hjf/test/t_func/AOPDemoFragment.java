package com.hjf.test.t_func;

import android.view.View;

import com.hjf.test.R;
import com.hjf.util.NotifyUtil;

import org.hjf.activity.BaseFragment;
import org.hjf.util.aop.SingleClick;

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
    }

    @Override
    @SingleClick
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_click:
                NotifyUtil.toast(++index + "- click");
                break;
        }
    }
}
