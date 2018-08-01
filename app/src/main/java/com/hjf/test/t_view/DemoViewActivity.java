package com.hjf.test.t_view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.hjf.test.R;

import org.hjf.activity.FragmentStackActivity;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class DemoViewActivity extends FragmentStackActivity {

    public static void start(Context context) {
        Intent intent = new Intent(context, DemoViewActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getFragmentContentId() {
        return R.id.fl_content;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_demo_view);
        addFragmentInBackStack(ViewMainFragment.newInstance(), false);
    }

}
