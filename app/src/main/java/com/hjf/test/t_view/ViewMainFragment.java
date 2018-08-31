package com.hjf.test.t_view;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hjf.TRouter;
import com.hjf.base.activity.BaseFragment;
import com.hjf.base.activity.FragmentStackActivity;
import com.hjf.test.R;
import com.hjf.test.TestStringRecyclerAdapter;

import org.hjf.view.recyclerview.OnViewClickListener;

import java.util.ArrayList;
import java.util.List;

public class ViewMainFragment extends BaseFragment {

    private TestStringRecyclerAdapter myAdapter;

    public static BaseFragment newInstance() {
        ViewMainFragment viewMainFragment = new ViewMainFragment();
        return viewMainFragment;
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
                FragmentStackActivity activity = (FragmentStackActivity) mActivityInBaseFragment;
                String data = myAdapter.getData(position);
                // Recycler View Swipe Demo
                if ("Recycler View Swipe Demo".equals(data)) {
                    activity.addFragmentInBackStack(RecyclerSiwpeItemFragment.newInstance(), true);
                }
                // Refresh Layout View Demo
                else if ("Refresh Layout View Demo".equals(data)) {
                    activity.addFragmentInBackStack(RefreshLayoutFragment.newInstance(), true);
                }
                // Screen Adapter Demo
                else if ("Screen Adapter Demo".equals(data)) {
                    TRouter.go(ScreenAdapterActivity.class);
                }
            }
        }, R.id.v_textView);
        myAdapter.setDataList(getDatas());
    }

    private List<String> getDatas() {
        List<String> strings = new ArrayList<>();
        strings.add("Recycler View Swipe Demo");
        strings.add("Refresh Layout View Demo");
        strings.add("Screen Adapter Demo");
        return strings;
    }
}
