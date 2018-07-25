package com.hjf.test.d_view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.hjf.test.R;

import org.hjf.activity.BaseFragment;
import org.hjf.view.recyclerview.AbsRecyclerAdapter;
import org.hjf.view.recyclerview.ViewHolder;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewFragment extends BaseFragment {

    private MyAdapter myAdapter;

    public static BaseFragment newInstance() {
        RecyclerViewFragment recyclerViewFragment = new RecyclerViewFragment();
        return recyclerViewFragment;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.v_recycler_view;
    }

    @Override
    public void bindView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
//        recyclerView.setLayoutManager(new GridLayoutManager(mActivityInBaseFragment, 2));
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
            ViewHolder.Build build = new ViewHolder.Build().setLayoutRes(R.layout.v_textview);
            // 偶数在右边，奇数在左边
            build.setSwipeModel(position % 2 == 0 ? ViewHolder.SwipeModel.RIGHT : ViewHolder.SwipeModel.LEFT);
            // 3的倍数自定义侧边栏
            if (position % 3 == 0) {
                build.setSwipeMenuLayoutRes(R.layout.item_swipe_menu_delete_collect);
            }
            // 5的倍数没有侧边栏
            if (position % 5 == 0) {
                build.setSwipeModel(ViewHolder.SwipeModel.NONE);
            }
            return build;
        }


        @Override
        protected void bindData2View(ViewHolder holder, final String data, final int position) {
            holder.setText(R.id.v_textView, data);
        }
    }

    private List<String> getDatas() {
        List<String> strings = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            String str = i % 2 == 0 ? "右边" : "左边";
            // 3的倍数自定义侧边栏
            if (i % 3 == 0) {
                str += " 自定义侧边菜单";
            } else {
                str += " 默认侧边菜单";
            }
            // 5的倍数没有侧边栏
            if (i % 5 == 0) {
                str = "没有侧边菜单";
            }
            strings.add(str);
        }
        return strings;
    }
}
