package com.hjf.ui.demo_view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.hjf.MyApp;
import com.hjf.TRouter;
import com.hjf.test.R;

import org.hjf.annotation.apt.Extra;
import org.hjf.annotation.apt.Router;
import org.hjf.util.ScreenAdapterUtils;

@Router()
public class ScreenAdapterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 屏幕适配，设计图 width = 360px
        ScreenAdapterUtils.adaptScreen4VerticalSlide(this, MyApp.getInstance(), 360);
        setContentView(R.layout.a_screen_adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ScreenAdapterUtils.cancelAdaptScreen(this, MyApp.getInstance());
    }
}
