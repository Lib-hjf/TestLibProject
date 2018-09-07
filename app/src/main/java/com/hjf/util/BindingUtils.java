package com.hjf.util;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class BindingUtils {

    @BindingAdapter({"imageUrl"})
    public static void loadImg(ImageView v, String url){
//        v.setColorFilter(v.getContext().getResources().getColor(SpUtil.isNight() ? R.color.CoverColor : R.color.colorWhite), PorterDuff.Mode.MULTIPLY);
        Glide.with(v.getContext())
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(v);
    }
}
