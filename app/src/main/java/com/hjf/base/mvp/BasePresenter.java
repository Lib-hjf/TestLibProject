package com.hjf.base.mvp;

public abstract class BasePresenter<V> {
    protected V mView;

    public void setView(V view) {
        this.mView = view;
        onAttached();
    }

    public abstract void onAttached();

    protected void onDetached() {
    }
}
