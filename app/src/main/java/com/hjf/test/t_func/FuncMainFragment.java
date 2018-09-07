package com.hjf.test.t_func;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hjf.base.activity.BaseFragment;
import com.hjf.base.activity.FragmentStackActivity;
import com.hjf.test.R;
import com.hjf.test.TestStringRecyclerAdapter;

import org.hjf.log.LogUtil;
import org.hjf.view.recyclerview.OnViewClickListener;

import java.util.ArrayList;
import java.util.List;

public class FuncMainFragment extends BaseFragment {

    private TestStringRecyclerAdapter myAdapter;

    public static BaseFragment newInstance() {
        FuncMainFragment funcMainFragment = new FuncMainFragment();
        return funcMainFragment;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.v_recycler_view;
    }

    @Override
    public void bindView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivityInBaseFragment));
        recyclerView.setAdapter(myAdapter = new TestStringRecyclerAdapter(mActivityInBaseFragment));
        myAdapter.setOnViewClickListener(new OnViewClickListener() {

            @Override
            public void onViewClickListener(View clickView, int position) {
                FragmentStackActivity activity = (FragmentStackActivity) mActivityInBaseFragment;
                String data = myAdapter.getData(position);
                if ("Wi-Fi Auto Switch".equals(data)) {
                    activity.addFragmentInBackStack(WiFiAutoSwitchDemoFragment.newInstance(), true);
                } else if ("AOP Test".equals(data)) {
                    activity.addFragmentInBackStack(AOPDemoFragment.newInstance(), true);
                } else if ("Log DB UI".equals(data)) {
                    LogUtil.gotoDBView();
                } else if ("MeshTask 、 LinkTask Test".equals(data)) {
                    activity.addFragmentInBackStack(TaskDemoFragment.newInstance(), true);
                }
            }
        }, R.id.v_textView);
        myAdapter.setDataList(getDatas());
    }

    private List<String> getDatas() {
        List<String> strings = new ArrayList<>();
        strings.add("AOP Test");
        strings.add("Log DB UI");
        strings.add("MeshTask 、 LinkTask Test");
//        strings.add("Wi-Fi Auto Switch");
        return strings;
    }
}
