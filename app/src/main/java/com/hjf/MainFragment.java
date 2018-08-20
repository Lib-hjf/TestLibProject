package com.hjf;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hjf.base.activity.BaseFragment;
import com.hjf.base.activity.FragmentStackActivity;
import com.hjf.test.R;
import com.hjf.test.TestStringRecyclerAdapter;
import com.hjf.test.t_conn.DemoConnActivity;
import com.hjf.test.t_func.FuncMainFragment;
import com.hjf.test.t_view.DemoViewActivity;

import org.hjf.view.recyclerview.OnViewClickListener;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends BaseFragment {

    private TestStringRecyclerAdapter myAdapter;

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
        RecyclerView recyclerView = findViewById(R.id.v_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivityInBaseFragment));
        recyclerView.setAdapter(myAdapter = new TestStringRecyclerAdapter(mActivityInBaseFragment));
        myAdapter.setOnViewClickListener(new OnViewClickListener() {
            @Override
            public void onViewClickListener(View clickView, int position) {
                String data = myAdapter.getData(position);
                // 自定义View Demo
                if ("CusView Demo".equals(data)) {
                    TRouter.addExtra("content", "Router Extra GET")
                            .go(DemoViewActivity.class);
                }
                // 长连接 Demo
                else if ("Connect Demo".equals(data)) {
                    TRouter.go(DemoConnActivity.class);
                }
                // 功能类测试
                else if ("Function Demo".equals(data)) {
                    ((FragmentStackActivity) mActivityInBaseFragment).addFragmentInBackStack(FuncMainFragment.newInstance(), true);
                }
            }
        }, R.id.v_textView);
        myAdapter.setDataList(getDatas());
    }


    private List<String> getDatas() {
        List<String> strings = new ArrayList<>();
        strings.add("Connect Demo");
        strings.add("CusView Demo");
        strings.add("Function Demo");
        return strings;
    }

}
