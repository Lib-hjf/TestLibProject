package com.hjf.base.adapter;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.hjf.view.recyclerview.BaseDataAdapter;
import org.hjf.view.recyclerview.BaseViewHolder;

import java.util.List;

/**
 * For multi layout. Read method note:
 * {@link BindingRecyclerAdapter#getItemLayoutRes(int)} and {@link RecyclerView.Adapter#getItemViewType(int)}
 * <p>
 * For item view holder warp up. Read method note:
 * {@link BindingRecyclerAdapter#getViewHolder(View, int)} and
 * {@link BindingRecyclerAdapter#getViewHolder4Bind(RecyclerView.ViewHolder)}
 * <p>
 */
@SuppressWarnings("unchecked")
public abstract class BindingRecyclerAdapter<M, B extends ViewDataBinding> extends BaseDataAdapter<M, RecyclerView.ViewHolder> {

    public BindingRecyclerAdapter(Context mContextInAdapter) {
        super(mContextInAdapter);
    }

    @Override
    public final RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContextInAdapter).inflate(getItemLayoutRes(viewType), parent, false);
        RecyclerView.ViewHolder BaseViewHolder = getViewHolder(view, viewType);
        DataBindingUtil.bind(getViewHolder4Bind(BaseViewHolder));
        return BaseViewHolder;
    }

    /**
     * warp up view holder
     *
     * @param layoutView   item layout resource from protected method {@link BaseDataAdapter#getItemLayoutRes(int)}
     * @param itemViewType item type form protected method {@link BaseDataAdapter#getItemViewType(int)}
     * @return YourViewHolder extends {@link BaseViewHolder}
     */
    protected RecyclerView.ViewHolder getViewHolder(View layoutView, int itemViewType) {
        return new org.hjf.view.recyclerview.BaseViewHolder(layoutView);
    }

    /**
     * For warp up binding layout.
     * If you wrap up the binding layout in your ViewHolder extends RecyclerView.ViewHolder.
     * Please override the method and return binding layout view.
     *
     * @param holder holder, {@link RecyclerView.ViewHolder#itemView} maybe warped up.
     * @return view inflater from binding layout.
     */
    @NonNull
    protected View getViewHolder4Bind(@NonNull RecyclerView.ViewHolder holder) {
        return holder.itemView;
    }

    @Override
    public final void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        this.onBindViewHolder(holder, position);
    }

    @Override
    public final void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        B binding = DataBindingUtil.getBinding(getViewHolder4Bind(holder));
        this.onBindViewHolder(holder, binding, getData(position), position);
    }

    protected abstract void onBindViewHolder(RecyclerView.ViewHolder holder, B viewDataBinding, M data, int position);
}
