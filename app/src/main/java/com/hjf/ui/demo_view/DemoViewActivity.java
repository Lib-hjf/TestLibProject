package com.hjf.ui.demo_view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.hjf.TRouter;
import com.hjf.base.activity.BaseFragment;
import com.hjf.base.mvp.BindingActivity;
import com.hjf.test.R;
import com.hjf.test.databinding.ActivityDemoViewBinding;
import com.hjf.util.NotifyUtil;
import com.hjf.view.RecyclerViewBindingWarp;

import org.hjf.annotation.apt.Extra;
import org.hjf.annotation.apt.Router;

import java.util.List;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
@Router()
public class DemoViewActivity extends BindingActivity<DemoViewPresenter, ActivityDemoViewBinding> implements DemoViewContract.View {

    // TODO 考虑是否能即时完成
    // TODO 该用内存变量，用之前从内存中查找，改变后即时更新内存
    @Extra("content")
    public String info;

    RecyclerViewBindingWarp<String> recyclerWarp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_demo_view);
        TRouter.bind(this);
        NotifyUtil.toast(info);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        recyclerWarp = new RecyclerViewBindingWarp<>((RecyclerView) mBinding.includeRecyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerWarp.mRecyclerView.setLayoutManager(manager);
        recyclerWarp.bindAdapter(R.layout.item_demo_view, mPresenter);
        mPresenter.loadData(0, 10);
    }

    @Override
    public void onLoadDataComplete(@NonNull List<String> dataList) {
        recyclerWarp.mAdapter.setDataList(dataList);
    }

    @Override
    public void showFragment(BaseFragment fragment) {
        if (fragment == null) {
            return;
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // 添加动画效果(进入：左出右进；退出：左进右出)，得在前面调用替换 Fragment 之前调用。
        transaction.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out, R.anim.slide_left_in, R.anim.slide_right_out);
        transaction.replace(R.id.fl_content, fragment, fragment.getClass().getName());
        // 将这个 transaction 加入BackStack，有Activity管理，按下返回键回到上一个Fragment
//        transaction.addToBackStack(fragment.getClass().getSimpleName());
        // 用户离开Activity之前会commit用户回复，在离开之后commit会抛不能回复此时提交内容的异常
        // 如果丢失也么关系，使用 commitAllowingStateLoss
        transaction.commitAllowingStateLoss();
    }

}
