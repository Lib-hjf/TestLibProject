package com.hjf.ui.demo_view;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hjf.base.activity.BaseFragment;
import com.hjf.test.R;
import com.hjf.util.NotifyUtil;

import org.hjf.log.LogUtil;
import org.hjf.view.recyclerview.AbsRecyclerAdapter;
import org.hjf.view.recyclerview.OnViewClickListener;
import org.hjf.view.recyclerview.OnViewLongClickListener;
import org.hjf.view.recyclerview.SideSlipCacheHolder;
import org.hjf.view.recyclerview.SideSlipLayout;
import org.hjf.view.recyclerview.ViewCacheHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * FIXME 删除后侧栏菜单没有复位
 */
public class RecyclerSwipeItemFragment extends BaseFragment {

    private MyAdapter myAdapter;

    public static BaseFragment newInstance() {
        RecyclerSwipeItemFragment recyclerSwipeItemFragment = new RecyclerSwipeItemFragment();
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
        myAdapter.setOnViewClickListener(new OnViewClickListener() {
            @Override
            public void onViewClickListener(View clickView, int position) {
                switch (clickView.getId()) {
                    case R.id.v_textView:
                        NotifyUtil.toast(myAdapter.getData(position).name);
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
        myAdapter.setDataList(getDatas());
    }


    private static class MyAdapter extends AbsRecyclerAdapter<ItemData> {

        MyAdapter(Context context) {
            super(context);
        }

        @Override
        protected int getItemLayoutRes(int itemViewType) {
            return R.layout.v_textview;
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
        @Override
        protected void onBindViewHolder(ViewCacheHolder holder, final ItemData data, final int position) {
            LogUtil.d("onBindViewHolder " + position);
            holder.setText(R.id.v_textView, data.name);
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

    public static class ItemData {
        @SideSlipLayout.SwipeModel
        private int swipeModel;
        private String name;
        @LayoutRes
        private int menuLayoutResCode;// 0 default  1 cus

        ItemData(int swipeModel, String name, int menuLayoutResCode) {
            this.swipeModel = swipeModel;
            this.name = name;
            this.menuLayoutResCode = menuLayoutResCode;
        }

        public int getSwipeModel() {
            return swipeModel;
        }

        public void setSwipeModel(int swipeModel) {
            this.swipeModel = swipeModel;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getMenuLayoutResCode() {
            return menuLayoutResCode;
        }

        public void setMenuLayoutResCode(int menuLayoutResCode) {
            this.menuLayoutResCode = menuLayoutResCode;
        }

        public int getCodeByModelAndMenuRes() {
            return swipeModel * 10 + menuLayoutResCode;
        }

        public static ItemData createByCode(int code) {
            return new ItemData(code / 10, "", code % 2);
        }

        @LayoutRes
        public int getLayoutResByResCode() {
            return menuLayoutResCode == 1 ? R.layout.item_swipe_menu_delete_collect : R.layout.item_swipe_menu_delete;
        }
    }
}
