package com.hjf.test;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.hjf.test.d_view.RecyclerViewFragment;

import org.hjf.activity.FragmentStackActivity;
import org.hjf.view.recyclerview.AbsRecyclerAdapter;
import org.hjf.view.recyclerview.ViewHolder;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentStackActivity {


    private RecyclerView recyclerView;
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_main);

        addContentFragment();
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
            addFragmentInBackStack(RecyclerViewFragment.newInstance(), false);
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

    private List<String> getDatas() {
        List<String> strings = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            strings.add(" data position " + i);
        }
        return strings;
    }


    private void stopDebug() {

    }

    private void restartDubug() {

    }

    private void exitDebugMode() {

    }

}
