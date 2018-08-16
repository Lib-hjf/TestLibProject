package com.hjf.test.t_view;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.hjf.TRouter;
import com.hjf.test.R;
import com.hjf.util.NotifyUtil;

import com.hjf.base.activity.FragmentStackActivity;
import org.hjf.annotation.apt.Extra;
import org.hjf.annotation.apt.Router;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
@Router()
public class DemoViewActivity extends FragmentStackActivity {

    @Extra("content")
    public String info;

    @Override
    public int getFragmentContentId() {
        return R.id.fl_content;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_demo_view);
        TRouter.bind(this);
        NotifyUtil.toast(info);
        addFragmentInBackStack(ViewMainFragment.newInstance(), false);
    }

}
