package com.hjf.ui.demo_view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hjf.base.activity.BaseFragment;
import com.hjf.base.adapter.BindingRecyclerAdapter;
import com.hjf.test.R;
import com.hjf.test.databinding.ItemRecyclerSwipeBinding;
import com.hjf.ui.demo_view.RecyclerSwipeItemFragment.ItemData;
import com.hjf.util.NotifyUtil;

import org.hjf.log.LogUtil;
import org.hjf.view.recyclerview.SideSlipCacheHolder;
import org.hjf.view.recyclerview.SideSlipLayout;
import org.hjf.view.recyclerview.ViewCacheHolder;

import java.util.ArrayList;
import java.util.List;

public class RecyclerSwipeItemFragmentDataBinding extends BaseFragment {

    private MyAdapter myAdapter;

    public static BaseFragment newInstance() {
        RecyclerSwipeItemFragmentDataBinding recyclerSwipeItemFragment = new RecyclerSwipeItemFragmentDataBinding();
        return recyclerSwipeItemFragment;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.v_recycler_view;
    }

    @Override
    public void bindView() {
        NotifyUtil.toast("长按条目添加数据");
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivityInBaseFragment));
        recyclerView.setAdapter(myAdapter = new MyAdapter(mActivityInBaseFragment));
        /*
        myAdapter.setOnViewClickListener(new OnViewClickListener() {
            @Override
            public void onViewClickListener(View clickView, int position) {
                switch (clickView.getId()) {
                    case R.id.v_textView:
                        NotifyUtil.toast(myAdapter.getData(position).getName());
                        break;
                    case R.id.tv_swipe_menu_delete:
                        myAdapter.removeData(position);
                        break;
                }
            }
        }, R.id.tv_swipe_menu_delete, R.id.v_textView);

        myAdapter.setOnViewLongClickListener(new OnViewLongClickListener() {
            @Override
            public boolean onViewLongClickListener(View clickView, int position) {
                List<ItemData> datas = new ArrayList<>();
                datas.add(new ItemData(SideSlipLayout.SwipeModel.NONE, "Auto Add1", 1));
                datas.add(new ItemData(SideSlipLayout.SwipeModel.NONE, "Auto Add2", 1));
                datas.add(new ItemData(SideSlipLayout.SwipeModel.NONE, "Auto Add3", 1));
                myAdapter.addDataList(position, datas);
                return true;
            }
        }, R.id.v_textView);
        */
        myAdapter.setDataList(getDatas());
    }

    private static class MyAdapter extends BindingRecyclerAdapter<ItemData, ItemRecyclerSwipeBinding> {

        MyAdapter(Context context) {
            super(context);
        }

        @Override
        protected int getItemLayoutRes(int itemViewType) {
            return R.layout.item_recycler_swipe;
        }

        @Override
        public int getItemViewType(int position) {
            ItemData data = getData(position);
            return data.getCodeByModelAndMenuRes();
        }

        @Override
        protected ViewCacheHolder getViewHolder(View layoutView, final int itemViewType) {
            ItemData data = ItemData.createByCode(itemViewType);
            SideSlipLayout swipeLayout = new SideSlipLayout(mContextInAdapter);
            swipeLayout.setSwipeModel(data.getSwipeModel());
            swipeLayout.setView(layoutView, data.getLayoutResByResCode());
            return new SideSlipCacheHolder(swipeLayout);
        }

        @NonNull
        @Override
        protected View getViewHolder4Bind(@NonNull RecyclerView.ViewHolder holder) {
            return ((SideSlipCacheHolder) holder).getContentView();
        }

        @Override
        protected void onBindViewHolder(RecyclerView.ViewHolder holder, ItemRecyclerSwipeBinding viewDataBinding, ItemData data, int position) {
            LogUtil.d("onBindViewHolder " + position);
            viewDataBinding.setItemData(data);
            viewDataBinding.executePendingBindings();
        }
    }

    private List<ItemData> getDatas() {
        List<ItemData> strings = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            int swipeModel;
            // 偶数在右边，奇数在左边
            String str = i % 2 == 0 ? "右边" : "左边";
            swipeModel = i % 2 == 0 ? SideSlipLayout.SwipeModel.RIGHT : SideSlipLayout.SwipeModel.LEFT;
            // 3的倍数自定义侧边栏
            if (i % 3 == 0) {
                str += " 自定义侧边菜单";
            } else {
                str += " 默认侧边菜单";
            }
            // 5的倍数没有侧边栏
            if (i % 5 == 0) {
                str = "没有侧边菜单";
                swipeModel = SideSlipLayout.SwipeModel.NONE;
            }
            strings.add(new ItemData(swipeModel, str + "  " + i, i % 3 == 0 ? 1 : 0));
        }
        return strings;
    }
}
