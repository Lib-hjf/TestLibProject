package com.hjf.ui.demo_view;

import com.hjf.TRouter;

import org.hjf.annotation.apt.InstanceFactory;

import java.util.ArrayList;
import java.util.List;

@InstanceFactory()
public class DemoViewPresenter extends DemoViewContract.Presenter {

    @Override
    public void showFragment(String tag) {
        // Recycler View Swipe Demo NoDataBinding
        if ("Recycler View Swipe Demo NoDataBinding".equals(tag)) {
            mView.showFragment(RecyclerSwipeItemFragment.newInstance());
        }
        // Recycler View Swipe Demo DataBinding
        if ("Recycler View Swipe Demo DataBinding".equals(tag)) {
            mView.showFragment(RecyclerSwipeItemFragmentDataBinding.newInstance());
        }
        // Refresh Layout View Demo
        else if ("Refresh Layout View Demo".equals(tag)) {
            mView.showFragment(RefreshLayoutFragment.newInstance());
        }
        // Screen Adapter Demo TODO
        else if ("Screen Adapter Demo".equals(tag)) {
            TRouter.go(ScreenAdapterActivity.class);
        }
    }

    @Override
    public void loadData(int startPosition, int num) {
        List<String> strings = new ArrayList<>();
        strings.add("Recycler View Swipe Demo NoDataBinding");
        strings.add("Recycler View Swipe Demo DataBinding");
        strings.add("Refresh Layout View Demo");
        strings.add("Screen Adapter Demo");
        mView.onLoadDataComplete(strings);
    }
}
