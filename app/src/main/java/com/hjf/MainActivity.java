package com.hjf;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hjf.test.R;
import com.hjf.test.t_func.AOPDemoFragment;

import org.hjf.activity.FragmentStackActivity;
import org.hjf.annotation.aspect.PermissionCheck;
import org.hjf.log.LogUtil;
import org.hjf.view.recyclerview.AbsRecyclerAdapter;
import org.hjf.view.recyclerview.ViewHolder;

public class MainActivity extends FragmentStackActivity {


    private RecyclerView recyclerView;
    private MyAdapter myAdapter;

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
        LogUtil.i("获取设备磁盘读写权限成功");
    }

    private void addContentFragment() {
        final boolean open_debug_mode = false;
        // 显示正常的主界面
        if (!open_debug_mode) {
            addFragmentInBackStack(MainFragment.newInstance(), false);
        }
        // 开启测试主界面
        else {
            // 添加显示测试用的Fragment
            addFragmentInBackStack(AOPDemoFragment.newInstance(), false);
            // 显示log信息展示区域
            intoDebugMode();
        }
    }


    @Override
    public int getFragmentContentId() {
        return R.id.fl_content;
    }

    /**
     * 进入Debug模式
     */
    private void intoDebugMode() {
        if (recyclerView == null) {
            recyclerView = findViewById(R.id.rcv_debug_log);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(myAdapter = new MyAdapter(this));
        }
        recyclerView.setVisibility(View.VISIBLE);
//        myAdapter.setDataList(getDatas());
        // 设置 Log 信息显示
    }

    private static class MyAdapter extends AbsRecyclerAdapter<String> {

        MyAdapter(Context context) {
            super(context);
        }

        @Override
        protected ViewHolder.Build getViewHolderBuild(int position) {
            return new ViewHolder.Build()
                    .setLayoutRes(R.layout.v_textview);
        }


        @Override
        protected void bindData2View(ViewHolder holder, String data, int position) {
            holder.setText(R.id.v_textView, data);
        }
    }

    private void stopDebug() {

    }

    private void restartDubug() {

    }

    private void exitDebugMode() {

    }
}
