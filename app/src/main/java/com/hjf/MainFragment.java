package com.hjf;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hjf.test.R;
import com.hjf.test.t_conn.DemoConnActivity;
import com.hjf.test.t_func.FuncMainFragment;
import com.hjf.test.t_view.DemoViewActivity;

import org.hjf.activity.BaseFragment;
import org.hjf.activity.FragmentStackActivity;
import org.hjf.view.recyclerview.AbsRecyclerAdapter;
import org.hjf.view.recyclerview.ViewHolder;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends BaseFragment {

    private MyAdapter myAdapter;

    public static BaseFragment newInstance() {
        MainFragment mainFragment = new MainFragment();
        return mainFragment;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.v_recycler_view;
    }

    @Override
    public void bindView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivityInBaseFragment));
        recyclerView.setAdapter(myAdapter = new MyAdapter(mActivityInBaseFragment));
        myAdapter.setDataList(getDatas());
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
        protected void bindData2View(ViewHolder holder, final String data, int position) {
            holder.setText(R.id.v_textView, data);
            holder.getItemView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 自定义View Demo
                    if ("CusView Demo".equals(data)) {
                        DemoViewActivity.start(mContextInAdapter);
                    }
                    // 长连接 Demo
                    else if ("Connect Demo".equals(data)) {
                        DemoConnActivity.start(mContextInAdapter);
                    }
                    // 功能类测试
                    else if ("Function Demo".equals(data)) {
                        ((FragmentStackActivity) mContextInAdapter).addFragmentInBackStack(FuncMainFragment.newInstance(), true);
                    }
                }
            });
        }
    }

    private List<String> getDatas() {
        List<String> strings = new ArrayList<>();
        strings.add("Connect Demo");
        strings.add("CusView Demo");
        strings.add("Function Demo");
        return strings;
    }

}
