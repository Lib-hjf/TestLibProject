package com.hjf.test.t_func;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hjf.test.R;

import org.hjf.activity.BaseFragment;
import org.hjf.activity.FragmentStackActivity;
import org.hjf.view.recyclerview.AbsRecyclerAdapter;
import org.hjf.view.recyclerview.ViewHolder;

import java.util.ArrayList;
import java.util.List;

public class FuncMainFragment extends BaseFragment {

    private MyAdapter myAdapter;

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
                    FragmentStackActivity activity = (FragmentStackActivity) mContextInAdapter;
                    if ("Wi-Fi Auto Switch".equals(data)) {
                        activity.addFragmentInBackStack(WiFiAutoSwitchDemoFragment.newInstance(), true);
                    }
                    else if ("AOP Test".equals(data)) {
                        activity.addFragmentInBackStack(AOPDemoFragment.newInstance(), true);
                    }
                }
            });
        }
    }

    private List<String> getDatas() {
        List<String> strings = new ArrayList<>();
        strings.add("AOP Test");
//        strings.add("Wi-Fi Auto Switch");
        return strings;
    }
}