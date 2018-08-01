package com.hjf.test.t_func;

import com.hjf.test.R;

import org.hjf.activity.BaseFragment;

/**
 * WiFi自动切换
 */
public class WiFiAutoSwitchDemoFragment extends BaseFragment {

    public static BaseFragment newInstance() {
        WiFiAutoSwitchDemoFragment switchDemoFragment = new WiFiAutoSwitchDemoFragment();
        return switchDemoFragment;
    }
    @Override
    public int getLayoutResId() {
        return R.layout.f_wifi_aotu_switch;
    }

    @Override
    public void bindView() {

    }

}
