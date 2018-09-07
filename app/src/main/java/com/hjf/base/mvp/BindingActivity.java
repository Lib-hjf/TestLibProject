package com.hjf.base.mvp;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.View;

import com.hjf.base.activity.BaseActivity;
import com.hjf.test.BR;

public abstract class BindingActivity<P extends BasePresenter, B extends ViewDataBinding> extends MVPActivity<P> {

    protected B mBinding;

    /**
     * 调用 {@link DataBindingUtil#setContentView(Activity, int)} 报错：
     * -----------    view tag isn't correct on view:null
     * <p>
     * 因为此方法会调用 {@link DataBindingUtil#bind(View)} 方法进行ViewBind，此时会对传入的 View.tag 进行校验
     * 同时此 View 为 {@link Activity#setContentView(int)} 是生成的View
     * 所以如果自行实现封装了Activity方法，此时获取到的 View 并不是LayoutRed顶层View，如 {@link BaseActivity#setContentView(int)}
     * 所以 BindView 时，View.Tag为null，校验失败
     * <p>
     * <p>
     * Tag 的添加等操作，博客： https://www.jianshu.com/p/de4d50b88437
     *
     * @param layoutResID activity content layout res id
     */
    @Override
    public void setContentView(int layoutResID) {
        View rootView = getLayoutInflater().inflate(layoutResID, null);
        this.setContentView(rootView);
    }

    @Override
    public void setContentView(View view) {
        mBinding = DataBindingUtil.bind(view);
        super.setContentView(view);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        // Activity binding-layout 中，data 标签传入了 Presenter 对象时，请用 presenter 作为标签
        // 不然就自己在代码里设置
        mBinding.setVariable(BR.presenter, mPresenter);
    }
}
