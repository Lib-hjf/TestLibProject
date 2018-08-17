package com.hjf.test;

import android.content.Context;

import org.hjf.view.recyclerview.AbsRecyclerAdapter;
import org.hjf.view.recyclerview.ViewHolder;

public class TestStringRecyclerAdapter extends AbsRecyclerAdapter<String> {

    public TestStringRecyclerAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutRes(int itemViewType) {
        return R.layout.v_textview;
    }

    @Override
    protected void onBindViewHolder(ViewHolder holder, String data, int position) {
        holder.setText(R.id.v_textView, data);
    }
}
