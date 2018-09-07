package com.hjf.ui.demo_view;

import android.support.annotation.NonNull;

import com.hjf.base.activity.BaseFragment;
import com.hjf.base.mvp.BasePresenter;
import com.hjf.base.mvp.BaseView;

import java.util.ArrayList;
import java.util.List;

public interface DemoViewContract {
    interface View extends BaseView {
        void showFragment(BaseFragment fragment);

        void onLoadDataComplete(@NonNull List<String> dataList);
    }

    abstract class Presenter extends BasePresenter<DemoViewContract.View> {

        @Override
        public void onAttached() {

        }

        public abstract void showFragment(String tag);

        public abstract void loadData(int startPosition, int num);

    }
}
