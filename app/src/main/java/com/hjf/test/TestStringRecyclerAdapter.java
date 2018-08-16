package com.hjf.test;

import android.content.Context;

import org.hjf.view.recyclerview.AbsRecyclerAdapter;
import org.hjf.view.recyclerview.ViewHolder;

public class TestStringRecyclerAdapter extends AbsRecyclerAdapter<String> {

    public TestStringRecyclerAdapter(Context context) {
        super(context);
    }

    @Override
    protected ViewHolder.Build getViewHolderBuild(int position) {
        return new ViewHolder.Build().setLayoutRes(R.layout.v_textview);
    }

    @Override
    protected void bindData2View(ViewHolder holder, String data, int position) {
        holder.setText(R.id.v_textView, data);
    }
}
