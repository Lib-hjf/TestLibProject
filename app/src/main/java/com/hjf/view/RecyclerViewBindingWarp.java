package com.hjf.view;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.hjf.base.adapter.BindingRecyclerAdapter;
import com.hjf.base.mvp.BasePresenter;
import com.hjf.test.BR;

import org.hjf.view.refresh.RefreshLayout;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * ViewDataBinding 和 RecyclerView 结合使用：
 * 1. 借助 ViewDataBinding 可实现 一个Adapter 使用在不同布局的RecyclerView
 * -    1.1 item layout 文件 data 标签中，实体类的引用固定为某字符串，如： itemData
 * -    1.2 单布局环境
 * <p>
 * <p>
 * <p>
 * 使用规定限制：
 * - 关于 ItemData 规定必须是：layout -> data -> variable -> name = itemData
 * - 关于 Presenter 规定必须是：layout -> data -> variable -> name = presenter
 */
public class RecyclerViewBindingWarp<M> {

    @Nullable
    public RefreshLayout mRefreshLayout;
    public RecyclerView mRecyclerView;
    public CommonAdapter<M> mAdapter;
    public AdapterPresenter<M> mAdapterPresenter;

    public RecyclerViewBindingWarp(@NonNull RefreshLayout refreshLayout) {
        if (!(refreshLayout.getContentView() instanceof RecyclerView)) {
            throw new RuntimeException("RefreshLayout content view must be RecyclerView");
        }
        this.mRefreshLayout = refreshLayout;
        this.mRecyclerView = (RecyclerView) refreshLayout.getContentView();
        init();
    }

    public RecyclerViewBindingWarp(@NonNull RecyclerView recyclerView) {
        this.mRecyclerView = recyclerView;
        init();
    }

    private void init() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mRecyclerView.getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        // Adapter Base Presenter
        mAdapterPresenter = new AdapterPresenter<>();
        // 刷新组件初始化设置
        if (mRefreshLayout != null) {
        }
    }

    @LayoutRes
    public int getItemLayoutRes() {
        return mAdapter.itemLayoutRes;
    }

    public void bindAdapter(@LayoutRes int itemLayoutRes, @Nullable BasePresenter presenter) {
        mAdapter = new CommonAdapter<>(mRecyclerView.getContext());
        this.mAdapter.itemLayoutRes = itemLayoutRes;
        this.mAdapter.presenter = presenter;
        this.mRecyclerView.setAdapter(this.mAdapter);
    }

    // adapter base presenter: loadData、PostParam
    public static class AdapterPresenter<M> {
        private HashMap<String, String> postParam = new HashMap<>();
        private List<M> dataList;

        public AdapterPresenter() {
            OkHttpClient okHttpClient = new OkHttpClient();
            Request.Builder builder = new Request.Builder();
            Call call = okHttpClient.newCall(builder.build());
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                }
            });
        }

    }

    // common adapter
    public static class CommonAdapter<M> extends BindingRecyclerAdapter<M, ViewDataBinding> {

        @LayoutRes
        int itemLayoutRes = -1;
        @Nullable
        BasePresenter presenter;

        CommonAdapter(Context mContextInAdapter) {
            super(mContextInAdapter);
        }

        @Override
        protected int getItemLayoutRes(int itemViewType) {
            return itemLayoutRes;
        }

        @Override
        protected void onBindViewHolder(RecyclerView.ViewHolder holder, ViewDataBinding viewDataBinding, M data, int position) {
            viewDataBinding.setVariable(BR.itemData, data);
            if (presenter != null) {
                viewDataBinding.setVariable(BR.presenter, presenter);
            }
            viewDataBinding.executePendingBindings();
        }
    }
}
