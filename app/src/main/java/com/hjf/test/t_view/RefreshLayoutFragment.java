package com.hjf.test.t_view;

import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hjf.base.activity.BaseFragment;
import com.hjf.test.R;
import com.hjf.test.TestStringRecyclerAdapter;
import com.hjf.util.NotifyUtil;

import org.hjf.thread.LinkTask;
import org.hjf.view.recyclerview.OnViewClickListener;
import org.hjf.view.refresh.OnLoadListener;
import org.hjf.view.refresh.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class RefreshLayoutFragment extends BaseFragment {

    private TestStringRecyclerAdapter myAdapter;
    private RefreshLayout refreshLayout;

    public static BaseFragment newInstance() {
        RefreshLayoutFragment refreshLayoutFragment = new RefreshLayoutFragment();
        return refreshLayoutFragment;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.v_recycler_refresh_view;
    }

    @Override
    public void bindView() {
        // refresh layout
        refreshLayout = findViewById(R.id.refresh_layout);
        refreshLayout.setLoadListener(new OnLoadListener() {
            @Override
            public void onRefresh() {
                new LinkTask()
                        .addRunnable(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1500);
                                } catch (InterruptedException ignored) {
                                }
                            }
                        })
                        .addRunnableInUIThread(new Runnable() {
                            @Override
                            public void run() {
                                myAdapter.setDataList(getDataList(0, 0));
                                refreshLayout.loadComplete();
                            }
                        })
                        .execute();
            }

            @Override
            public void onLoadMore() {
                new LinkTask()
                        .addRunnable(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(2000);
                                } catch (InterruptedException ignored) {
                                }
                            }
                        })
                        .addRunnableInUIThread(new Runnable() {
                            @Override
                            public void run() {
                                myAdapter.addDataList(getDataList(myAdapter.getItemCount(), 20));
                                refreshLayout.loadComplete();
                            }
                        })
                        .execute();
            }
        });
        View emptyView = refreshLayout.getEmptyView();
        if (emptyView != null) {
            TextView textView = emptyView.findViewById(R.id.tv_hint);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO 现在加载动画
                    myAdapter.setDataList(getDataList(0, 10));
                }
            });
        }
        // recycler view
        RecyclerView recyclerView = findViewById(R.id.v_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivityInBaseFragment));
        // recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(myAdapter = new TestStringRecyclerAdapter(mActivityInBaseFragment));
        myAdapter.setOnViewClickListener(new OnViewClickListener() {
            @Override
            public void onViewClickListener(View clickView, int position) {
                switch (clickView.getId()) {
                    case R.id.v_textView:
                        NotifyUtil.toast(myAdapter.getData(position));
                        break;
                }
            }
        }, R.id.v_textView);

        // query data
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                myAdapter.setDataList(getDataList(0, 0));
            }
        }, 10);
    }

    private static List<String> getDataList(int startIndex, int num) {
        List<String> dataList = new ArrayList<>();
        for (int i = startIndex; i < startIndex + num; i++) {
            dataList.add("item data - " + i);
        }
        return dataList;
    }
}
