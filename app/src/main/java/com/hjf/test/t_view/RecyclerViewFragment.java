package com.hjf.test.t_view;

import android.content.Context;
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
import org.hjf.view.recyclerview.SwipeHolder;
import org.hjf.view.recyclerview.SwipeLayout;
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
        NotifyUtil.toast("长按条目添加数据");
        RecyclerView recyclerView = findViewById(R.id.v_recyclerView);
//        recyclerView.setLayoutManager(new GridLayoutManager(mActivityInBaseFragment, 2));
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivityInBaseFragment));
        recyclerView.setAdapter(myAdapter = new MyAdapter(mActivityInBaseFragment));
        myAdapter.setOnViewClickListener(new OnViewClickListener() {
            @Override
            public void onViewClickListener(View view, int position) {
                switch (view.getId()) {
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
            public boolean onViewLongClickListener(View view, int position) {
                List<ItemData> datas = new ArrayList<>();
                datas.add(new ItemData(SwipeLayout.SwipeModel.NONE, "Auto Add1", 1));
                datas.add(new ItemData(SwipeLayout.SwipeModel.NONE, "Auto Add2", 1));
                datas.add(new ItemData(SwipeLayout.SwipeModel.NONE, "Auto Add3", 1));
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
        protected int getLayoutRes(int itemViewType) {
            return R.layout.v_textview;
        }

        @Override
        protected ViewHolder getViewHolder(View layoutView, final int itemViewType) {
            int[] modelAndMenuRes = ItemData.getModelAndMenuResByCode(itemViewType);

            SwipeLayout swipeLayout = new SwipeLayout(mContextInAdapter);
            swipeLayout.setSwipeModel(modelAndMenuRes[0]);
            swipeLayout.setView(layoutView, modelAndMenuRes[1]);

            return new SwipeHolder(swipeLayout);
        }

        @Override
        public int getItemViewType(int position) {
            ItemData data = getData(position);
            return ItemData.getCodeByModelAndMenuRes(data.swipeModel, data.menuLayoutResCode);
        }

        @Override
        protected void onBindViewHolder(ViewHolder holder, final ItemData data, final int position) {
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
            swipeModel = i % 2 == 0 ? SwipeLayout.SwipeModel.RIGHT : SwipeLayout.SwipeModel.LEFT;
            // 3的倍数自定义侧边栏
            if (i % 3 == 0) {
                str += " 自定义侧边菜单";
            } else {
                str += " 默认侧边菜单";
            }
            // 5的倍数没有侧边栏
            if (i % 5 == 0) {
                str = "没有侧边菜单";
                swipeModel = SwipeLayout.SwipeModel.NONE;
            }
            strings.add(new ItemData(swipeModel, str + "  " + i, i % 3 == 0 ? 2 : 1));
        }
        return strings;
    }

    private static class ItemData {
        @SwipeLayout.SwipeModel
        private int swipeModel;
        private String name;
        private int menuLayoutResCode;// 1 default  2 cus

        ItemData(int swipeModel, String name, int menuLayoutResCode) {
            this.swipeModel = swipeModel;
            this.name = name;
            this.menuLayoutResCode = menuLayoutResCode;
        }

        private static int getCodeByModelAndMenuRes(@SwipeLayout.SwipeModel int model, int menuLayoutResCode) {
            return model * 10 + menuLayoutResCode;
        }


        private static int[] getModelAndMenuResByCode(int code) {
            int res = code % 10 == 2 ? R.layout.item_swipe_menu_delete_collect : R.layout.item_swipe_menu_delete;
            return new int[]{code / 10, res};
        }
    }
}
