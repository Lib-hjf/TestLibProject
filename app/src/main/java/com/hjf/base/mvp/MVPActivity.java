package com.hjf.base.mvp;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.hjf.InstanceFactory;
import com.hjf.base.activity.BaseActivity;

import org.hjf.log.LogUtil;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@SuppressWarnings("unchecked")
public abstract class MVPActivity<P extends BasePresenter> extends BaseActivity {

    protected P mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPresenter();
    }

    protected void initPresenter() {
        LogUtil.v("Presenter init start");
        if (this instanceof BaseView) {
            LogUtil.v("Presenter init : instanceof BaseView   --- OK.");
            // 获取带有泛型的父类
            if (this.getClass().getGenericSuperclass() instanceof ParameterizedType) {
                LogUtil.v("Presenter init : instanceof ParameterizedType   --- OK.");
                // 请将泛型 BasePresenter放在第一个
                ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
                if (type.getActualTypeArguments().length > 0) {
                    LogUtil.v("Presenter init : instanceof ParameterizedType.length>0   --- OK.");
                    Class mPresenterClass = (Class) type.getActualTypeArguments()[0];
                    try {
                        mPresenter = InstanceFactory.create(mPresenterClass);
                        LogUtil.v("Presenter init ok.");
                    } catch (InstantiationException | IllegalAccessException ignored) {
                    }
                }
            }
        }
        if (mPresenter == null) {
            LogUtil.e("Presenter init error.");
            return;
        }
        mPresenter.setView(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onDetached();
        }
    }
}
