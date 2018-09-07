package com.hjf.test;

import android.content.Context;

import org.hjf.view.recyclerview.AbsRecyclerAdapter;
import org.hjf.view.recyclerview.ViewCacheHolder;

public class TestStringRecyclerAdapter extends AbsRecyclerAdapter<String> {

    public TestStringRecyclerAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getItemLayoutRes(int itemViewType) {
        return R.layout.v_textview;
    }

    @Override
    protected void onBindViewHolder(ViewCacheHolder holder, String data, int position) {
        holder.setText(R.id.v_textView, data);
    }
}
