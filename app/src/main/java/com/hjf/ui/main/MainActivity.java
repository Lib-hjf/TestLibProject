package com.hjf.ui.main;

import android.Manifest;
import android.os.Bundle;

import com.hjf.base.activity.BaseFragment;
import com.hjf.base.activity.FragmentStackActivity;
import com.hjf.test.R;

import org.hjf.annotation.aspect.PermissionCheck;
import org.hjf.log.LogUtil;

public class MainActivity extends FragmentStackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_main);

        addContentFragment();

        forLog2Disk();
        LogUtil.setMainThreadId(Thread.currentThread().getId());
    }

    /**
     * 适配 6.0 log日志打印
     */
    @PermissionCheck({Manifest.permission.WRITE_EXTERNAL_STORAGE})
    private void forLog2Disk() {
        LogUtil.v("获取设备磁盘读写权限成功");
    }

    private void addContentFragment() {
        BaseFragment testFragment = null;
//        testFragment = RefreshLayoutFragment.newInstance();

        // 开启测试主界面
        if (testFragment != null) {
            // 添加显示测试用的Fragment
            addFragmentInBackStack(testFragment, false);
            return;
        }

        // 显示正常的主界面
        addFragmentInBackStack(MainFragment.newInstance(), false);
    }


    @Override
    public int getFragmentContentId() {
        return R.id.fl_content;
    }
}
